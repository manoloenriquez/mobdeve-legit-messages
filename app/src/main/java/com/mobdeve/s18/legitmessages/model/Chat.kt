package com.mobdeve.s18.legitmessages.model

class Chat(val chatId: String) {

    var lastMessage: String = ""
    var lastDate: String = ""
    var label: String = ""
    val participants: ArrayList<String> = ArrayList()
    lateinit var messages: ArrayList<Message>

//    constructor(chatId: String, username: String, lastMessage: String, lastDate: String) {
//        this.chatId = chatId
//        this.username = username
//        this.lastMessage = lastMessage
//        this.lastDate = lastDate
//    }

//    constructor(chatId: String, username: String) {
//        this.chatId = chatId
//        this.username = username
//    }

    init {
        this.chatId
    }

    fun usernamesString(): String {
        var out = ""

        participants.forEach { username ->
            out += "$username, "
        }

        out.trim()

        return out
    }
}