package com.mobdeve.s18.legitmessages.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.ui.chats.ChatAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class Database {

    val db = Firebase.firestore

    fun setUser(user: User) {
        val map = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "firstName" to user.firstName,
            "lastName" to user.lastName
        )
        user.uid?.let { db.collection("users").document(it).set(map) }
    }

//    fun getUser(uid: String) {
//        db.collection("users").document(uid).get()
//            .addOnSuccessListener { document ->
//                if (document == null) {
//                    return@addOnSuccessListener
//                }
//
//                Log.i("Database", "User: ${document.data}")
//            }
//    }

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

        // Check if contact exists already
//        currentUserRef
//            .collection("contacts")
//            .whereEqualTo("user", contactRef)
//            .get()
//            .await()

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

//    suspend fun getMessages(chatId: String): ArrayList<Message> {
//        val messages = ArrayList<Message>()
//        Log.d("Messages", chatId)
//        val ref = db.collection("chats")
//                    .document(chatId)
//                    .collection("messages")
//                    .orderBy("timeStamp")
//        val snapshot = ref.get().await()
//
//        snapshot.documents.forEach { document ->
//            withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
//                val data = document.data
//                val sender = (data?.get("sender") as DocumentReference).id
//
//                val timeStamp = data.get("timeStamp") as Timestamp
//
//                val message = Message(
//                    sender,
//                    data.get("message") as String,
//                    timeStamp
//                )
//
//                messages.add(message)
//            }
//        }
//
//        return messages
//    }


//    suspend fun loadChats() {
//        val currentUid: String? = User.currentUser!!.uid
//        val chats = ArrayList<Chat>()
//        val userRef: DocumentReference? = currentUid?.let { db.collection("users").document(it) }
//        val receivers = hashMapOf<String, String>()
//
//        if (userRef != null) {
//            db.collection("chats")
//                .whereArrayContains("participants", userRef)
//                .limit(100)
//                .addSnapshotListener { value, error ->
//
//                    value?.documents?.forEach { document ->
//                        val data = document.data
//                        val chat = Chat(document.id)
//
//                        (data?.get("participants") as ArrayList<DocumentReference>).forEach { userRef ->
//                            CoroutineScope(Dispatchers.Main).launch {
//                                if (receivers.get(userRef.id) != null) {
//                                    chat.participants.add(receivers.get(userRef.id)!!)
//                                } else if (userRef.id != currentUid) {
//                                    val user = userRef.get().await()
//                                    val username: String = user.data?.get("username") as String
//
//                                    receivers.put(userRef.id, username)
//
//                                    chat.participants.add(receivers.get(userRef.id)!!)
//                                }
//
//                                Log.d("Chats", "${chat.participants}")
//                            }
//                        }
//                        chats.add(chat)
//                    }
//                }
//
//            User.currentUser!!.chats = chats
//            Log.d("Chats", "${User.currentUser!!.chats}")
//        }
//    }

    fun addMessage(chatUid: String, message: Message) {
        val ref = db.collection("chats").document(chatUid)
        val data = hashMapOf(
            "message" to message.message,
            "sender" to db.collection("users").document(message.sender),
            "timeStamp" to message.timeStamp
        )

        ref.collection("messages").add(data)
    }
}
