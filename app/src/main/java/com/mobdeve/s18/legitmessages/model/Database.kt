package com.mobdeve.s18.legitmessages.model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
}
