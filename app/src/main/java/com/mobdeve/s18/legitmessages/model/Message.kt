package com.mobdeve.s18.legitmessages.model

class Message {

    var sender: String = ""
    var message: String = ""
    var timeStamp: String = ""

    constructor(sender: String, message: String, timeStamp: String) {
        this.sender = sender
        this.message = message
        this.timeStamp = timeStamp
    }

    constructor(message: String, timeStamp: String) {
        this.message = message
        this.timeStamp = timeStamp
    }


}