package com.mobdeve.s18.legitmessages.model

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        Log.i("Contacts", "Fetching contacts")
        User.currentUser?.uid?.let { db.collection("users").document(it).collection("contacts") }
            ?.addSnapshotListener { snapshot, e ->
                Log.i("Before clearing", "${User.currentUser?.contacts}")
                User.currentUser?.contacts?.clear()
                Log.i("After clearing", "${User.currentUser?.contacts}")
                snapshot?.documents?.forEach { document ->
                    val docData = document.data
                    val userRef: DocumentReference = docData?.get("user") as DocumentReference
                    var data: DocumentSnapshot? = null

                    CoroutineScope(Dispatchers.Main).launch {
                        data = userRef.get().await()
                        Log.i("User", "${data}")

                        User.currentUser!!.contacts.add(
                            User(
                                document.id,
                                data?.get("email") as String?,
                                "${data?.get("firstName")} ${data?.get("lastName")}",
                                data?.get("firstName") as String,
                                data!!["lastName"] as String,
                                data!!["username"] as String
                            )
                        )
                    }
                }
                Log.i("Contacts", "Done fetching contacts")
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

        return true
    }
}
