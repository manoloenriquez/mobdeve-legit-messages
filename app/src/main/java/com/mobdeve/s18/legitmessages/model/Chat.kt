package com.mobdeve.s18.legitmessages.model

class Chat(val chatId: String) {

    var lastMessage: String = ""
    var lastDate: String = ""
    var label: String = ""
    val participants: ArrayList<String> = ArrayList()
    lateinit var messages: ArrayList<Message>

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