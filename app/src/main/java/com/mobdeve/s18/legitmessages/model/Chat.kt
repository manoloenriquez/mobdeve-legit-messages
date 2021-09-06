package com.mobdeve.s18.legitmessages.model

class Chat {

    var chatId: String = ""
    var username: String = ""
    var lastMessage: String = ""
    var lastDate: String = ""

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
}