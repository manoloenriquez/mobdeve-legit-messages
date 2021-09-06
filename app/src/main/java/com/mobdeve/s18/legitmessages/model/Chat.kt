package com.mobdeve.s18.legitmessages.model

class Chat {

    var chatId: String = ""
    var username: String = ""
    var lastMessage: String = ""
    var lastDate: String = ""

    val participants: ArrayList<String> = ArrayList()

    constructor(chatId: String, username: String, lastMessage: String, lastDate: String) {
        this.chatId = chatId
        this.username = username
        this.lastMessage = lastMessage
        this.lastDate = lastDate
    }

    constructor(chatId: String, username: String) {
        this.chatId = chatId
        this.username = username
    }

    constructor(chatId: String) {
        this.chatId
    }

    fun usernamesString(): String {
        var out = ""

        participants.forEach { username ->
            out += "$username "
        }

        out.trim()

        return out
    }
}