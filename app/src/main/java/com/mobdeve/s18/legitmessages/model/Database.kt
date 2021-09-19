package com.mobdeve.s18.legitmessages.model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toFile
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.File

class Database {

    val db = Firebase.firestore
    val storage = Firebase.storage

    fun setUser(user: User) {
        val map = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "firstName" to user.firstName,
            "lastName" to user.lastName
        )
        user.uid?.let { db.collection("users").document(it).set(map) }
    }

    fun updateUser(user: User) {
        val map = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "firstName" to user.firstName,
            "lastName" to user.lastName
        )
        user.uid?.let { db.collection("users").document(it).update(map as Map<String, Any>) }
    }

    suspend fun getUser(uid: String): DocumentSnapshot = db
        .collection("users")
        .document(uid)
        .get()
        .await()

     suspend fun getUserContacts(uid: String): ArrayList<User> {
         Log.i("DatabaseUsers", "Started fetching")
         val users = ArrayList<User>()
         val currentUser = getUser(uid)
         val contacts = currentUser.reference.collection("contacts").get().await()
         contacts.documents.forEach { contact ->
             val userDoc = (contact.get("user") as DocumentReference).get().await()
             val data = userDoc.data

             val user = User(
                 userDoc.id,
                 data?.get("email") as String?,
                 "${data?.get("firstName")} ${data?.get("lastName")}",
                 data?.get("firstName") as String,
                 data["lastName"] as String,
                 data["username"] as String
             )
             Log.i("DatabasseUser", "${user.displayName}")
             users.add(user)
         }

         Log.i("DatabaseUsers", "$users")
         return users
    }

    suspend fun searchUser(key: String): ArrayList<User> {
        val users = ArrayList<User>()

        val result = db.collection("users")
            .whereGreaterThanOrEqualTo("username", key)
            .whereLessThanOrEqualTo("username", "${key}\uF7FF")
            .get()
            .await()

        result.documents.forEach { document ->
            val data = document.data
            val user = User(
                document.id,
                data?.get("email") as String?,
                "${data?.get("firstName")} ${data?.get("lastName")}",
                data?.get("firstName") as String,
                data.get("lastName") as String,
                data.get("username") as String
            )

            users.add(user)
        }

        return users
    }

    fun loadContacts() {

        User.currentUser?.uid?.let { db.collection("users").document(it).collection("contacts") }
            ?.addSnapshotListener { snapshot, e ->
                CoroutineScope(Dispatchers.Main).launch {
                    Log.i("Contacts", "Fetching contacts")
                    User.currentUser?.contacts?.clear()

                    snapshot?.documents?.forEach { document ->
                        val docData = document.data
                        val userRef: DocumentReference = docData?.get("user") as DocumentReference
                        var data: DocumentSnapshot? = null


                        data = userRef.get().await()

                        User.currentUser!!.contacts.add(
                            User(
                                userRef.id,
                                data?.get("email") as String?,
                                "${data?.get("firstName")} ${data?.get("lastName")}",
                                data?.get("firstName") as String,
                                data["lastName"] as String,
                                data["username"] as String
                            )
                        )
                    }
                    Log.i("Contacts", "Done fetching contacts")
                }
            }

    }

    suspend fun addContact(currentUid: String, contactUid: String): Boolean {
        val contactRef: DocumentReference = db.collection("users").document(contactUid)
        val currentUserRef = db.collection("users").document(currentUid)

        // Add to contact list
        val map = hashMapOf(
            "user" to contactRef
        )
        currentUserRef
            .collection("contacts")
            .add(map)
            .await()

        return true
    }

    suspend fun deleteContact(currentUid: String, contactUid: String): Boolean {
        val contactRef: DocumentReference = db.collection("users").document(contactUid)
        val currentUserRef = db.collection("users").document(currentUid)


        val result = currentUserRef.collection("contacts")
            .whereEqualTo("user", contactRef)
            .get()
            .await()

        Log.i("Results","${result.documents}")
        Log.i("ContactRef", "$contactRef")

        result.documents[0].reference.delete()

        return true
    }

    suspend fun getChats(): ArrayList<Chat> {
        val currentUid: String? = User.currentUser!!.uid
        val chats = ArrayList<Chat>()
        val userRef: DocumentReference? = currentUid?.let { db.collection("users").document(it) }
        val receivers = hashMapOf<String, String>()

        if (userRef != null) {
            val chatsRef = db.collection("chats")
                .whereArrayContains("participants", userRef)
                .limit(100)
                .get()
                .await()

            chatsRef.documents.forEach { document ->
                CoroutineScope(Dispatchers.Main).async {
                    val data = document.data
                    val chat = Chat(document.id)
                    Log.i("Chat id", chat.chatId)

                    (data?.get("participants") as ArrayList<DocumentReference>).forEach { userRef ->
                        withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                            if (receivers.get(userRef.id) != null) {
                                chat.participants.add(receivers.get(userRef.id)!!)
                            } else if (userRef.id != currentUid) {
                                val user = userRef.get().await()
                                val username: String = user.data?.get("username") as String
                                receivers.put(userRef.id, username)

                                chat.participants.add(receivers.get(userRef.id)!!)
                            }
                        }
                    }

                    chat.label = chat.usernamesString()
                    Log.d("Chats", chat.label)
                    chats.add(chat)
                    Log.d("Chats", "$chats")
                }.await()
            }
        }

        return chats

    }

    suspend fun createChat(participants: ArrayList<String>): String {
        val userRefs: ArrayList<DocumentReference> = ArrayList()

        for (participant in participants) {
            val ref = db.collection("users").document(participant)

            userRefs.add(ref)
        }

        val data = hashMapOf(
            "participants" to userRefs
        )

        val result = db.collection("chats").add(data).await()

        return result.id
    }

    fun addMessage(chatUid: String, message: Message) {
        val ref = db.collection("chats").document(chatUid)
        val data = hashMapOf(
            "message" to message.message,
            "sender" to db.collection("users").document(message.sender),
            "timeStamp" to message.timeStamp
        )

        val task = ref.collection("messages").add(data)
        task.addOnSuccessListener { result ->
            message.id = result.id
        }
    }

    fun editMessage(chatUid: String, messageUid: String, message: String) {
        val ref =
            db.collection("chats")
                .document(chatUid)
                .collection("messages")
                .document(messageUid)

        val data = hashMapOf(
            "message" to message,
            "sender" to User.currentUser?.uid?.let { db.collection("users").document(it) },
        )

        ref.update(data)
    }

    fun deleteMessage(chatUid: String, messageUid: String) {
        val ref =
            db.collection("chats")
                .document(chatUid)
                .collection("messages")
                .document(messageUid)

        ref.delete()
    }

    fun addChat(participants: ArrayList<String>) {
        val participantsRef: ArrayList<DocumentReference> = ArrayList()

        participants.forEach { p ->
            val ref = db.collection("users").document(p)
            participantsRef.add(ref)
        }

        val data = hashMapOf(
            "participants" to participantsRef
        )

        db.collection("chats").add(data)
    }

    suspend fun addImageMessage(chatUid: String, message: ImageMessage) {
        val img = message.uri
        val file = File(img.path!!)
        val storageRef = storage.reference
        val imgRef = storageRef.child("$chatUid/${file.name}")

        val uploadTask = imgRef.putFile(img)

        uploadTask.await()

        val url = imgRef.downloadUrl.await()

        Log.i("URL", url.toString())

        val ref = db.collection("chats").document(chatUid)
        val data = hashMapOf(
            "image" to url.toString(),
            "sender" to db.collection("users").document(message.sender),
            "timeStamp" to message.timeStamp
        )

        val result = ref.collection("messages").add(data).await()
        message.id = result.id


//        uploadTask.addOnSuccessListener { snapshot ->
//            imgRef.downloadUrl.addOnCompleteListener { url ->
//                Log.i("URL", url.toString())
//
//                val ref = db.collection("chats").document(chatUid)
//                val data = hashMapOf(
//                    "image" to url.toString(),
//                    "sender" to db.collection("users").document(message.sender),
//                    "timeStamp" to message.timeStamp
//                )
//
//                val task = ref.collection("messages").add(data)
//
//                task.addOnSuccessListener { result ->
//                    message.id = result.id
//                }
//            }
//        }
    }

    suspend fun getParticipants(chatUid: String): ArrayList<User> {
        val participants: ArrayList<User> = ArrayList()
        val ref = db.collection("chats").document(chatUid)
        val snapshot = ref.get().await()
        val data = snapshot.data

        if (data != null) {
            for (participant in (data.get("participants") as ArrayList<DocumentReference>)) {
                val userRef = db.collection("users").document(participant.id)
                val userSnapshot = userRef.get().await()
                val userData = userSnapshot.data

                val user = User(
                    userSnapshot.id,
                    userData?.get("email") as String?,
                    "${userData?.get("firstName")} ${userData?.get("lastName")}",
                    userData?.get("firstName") as String,
                    userData.get("lastName") as String,
                    userData.get("username") as String

                )

                participants.add(user)
            }
        }

        return participants
    }

    suspend fun getChatIdWithContact(contactUid: String): String? {
        val userUid = User.currentUser?.uid ?: return null
        var chatId: String? = null

        val userRef = db.collection("users").document(userUid)
        val contactRef = db.collection("users").document(contactUid)

        val ref = db.collection("chats")
                    .whereArrayContainsAny("participants", listOf(userRef, contactRef))

        val snapshot = ref.get().await()

        snapshot.documents.forEach { document ->
            val data = document.data
            val participants = data?.get("participants") as ArrayList<DocumentReference>

            if (participants.contains(userRef) && participants.contains(contactRef) && participants.size == 2) {
                chatId = document.id
            }
        }

        return chatId
    }

    suspend fun searchChat(chatUid: String, key: String): ArrayList<Message> {
        val ref =
            db.collection("chats").document(chatUid).collection("messages")

        val result =
            ref.whereGreaterThanOrEqualTo("message", key)
                .whereLessThanOrEqualTo("message", "${key}\uF7FF")
                .get()
                .await()

        val messages: ArrayList<Message> = ArrayList()

        result.documents.forEach { document ->
            val data = document.data
            val senderRef: DocumentReference = data?.get("sender") as DocumentReference
            val body: String = data.get("message") as String
            val timestamp: Timestamp = data.get("timeStamp") as Timestamp

            val message = Message(senderRef.id, body, timestamp, document.id, chatUid)

            messages.add(message)
        }

        Log.i("Search Message", "$messages")

        return messages
    }

    suspend fun removeParticipant(chatUid: String, participants: ArrayList<String>) {
        val ref = db.collection("chats").document(chatUid)
        val snapshot = ref.get().await()
        val data = snapshot.data

        val oldParticipants: ArrayList<DocumentReference> = data?.get("participants") as ArrayList<DocumentReference>
        val newParticipants: ArrayList<DocumentReference> = ArrayList()

        oldParticipants.forEach old@ { participant ->
            if (!participants.contains(participant.id) || participant.id == User.currentUser?.uid) {
                newParticipants.add(participant)
            }
        }

        val toUpdate = hashMapOf(
            "participants" to newParticipants
        )

        ref.update(toUpdate as Map<String, Any>).await()
    }

    suspend fun addParticipant(chatUid: String, participantId: String) {
        val chatRef = db.collection("chats").document(chatUid)
        val userRef = db.collection("users").document(participantId)

        val chatDocument = chatRef.get().await()
        val chatData = chatDocument.data

        val participants: ArrayList<DocumentReference> = chatData?.get("participants") as ArrayList<DocumentReference>

        participants.add(userRef)

        val toUpdate = hashMapOf(
            "participants" to participants
        )

        chatRef.update(toUpdate as Map<String, Any>).await()
    }
}
