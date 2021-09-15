package com.mobdeve.s18.legitmessages.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class Notifications : FirebaseMessagingService() {

    var TAG = "FCMService"
    override fun onNewToken(p0: String) {
        Log.d(TAG, p0)
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d(TAG, "Payloads : ${p0.data}")

        if(p0.notification != null){
            val title = p0.notification!!.title
            val body = p0.notification!!.body

            Log.d(TAG, "Title - $title, Body - $body")
        }
    }
}