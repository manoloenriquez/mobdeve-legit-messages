package com.mobdeve.s18.legitmessages.model

import com.google.firebase.Timestamp
import java.util.*
import kotlin.time.hours

open class Message {

    var sender: String = ""
    var message: String = ""
    var timeStamp: Timestamp

    constructor(sender: String, message: String, timeStamp: Timestamp) {
        this.sender = sender
        this.message = message
        this.timeStamp = timeStamp
    }

    constructor(sender: String, message: String) {
        this.sender = sender
        this.message = message
        this.timeStamp = Timestamp.now()
    }


    open fun send(chatId: String) {
        val db = Database()

        db.addMessage(chatId, this)
    }

    fun timeStampString(): String {
        val calendar = Calendar.getInstance()
        calendar.time = timeStamp.toDate()
        var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val meridian: String

        if ((hour >=0) && (hour <= 11))
            meridian = "AM"
        else{
            hour -= 12
            meridian = "PM"
        }

        return "$hour:$minute $meridian"
    }
}