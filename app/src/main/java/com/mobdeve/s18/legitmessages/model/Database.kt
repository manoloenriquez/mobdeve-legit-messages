package com.mobdeve.s18.legitmessages.model

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
}