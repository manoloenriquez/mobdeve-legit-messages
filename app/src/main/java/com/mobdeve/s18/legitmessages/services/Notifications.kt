package com.mobdeve.s18.legitmessages.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobdeve.s18.legitmessages.MainActivity
import java.util.*

class Notifications : FirebaseMessagingService() {
    lateinit var title: String
    lateinit var message: String
    var CHANNEL_ID = "CHANNEL"

    lateinit var manager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("message", "new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        title = remoteMessage.data["title"]!!
        message= remoteMessage.data["Message"]!!

        if(message == null){
            message = Objects.requireNonNull(remoteMessage.notification!!.body)!!
        }
        var manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        sendNotification()
    }

    private fun sendNotification() {
        var intent = Intent(application, MainActivity::class.java)

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            var channel = NotificationChannel(CHANNEL_ID,
                "pushnotification",
                NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setContentText(message)

        var pendingintent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent, PendingIntent.FLAG_ONE_SHOT)

        builder.setContentIntent(pendingintent)
        manager.notify(0, builder.build())

    }

}