package com.mobdeve.s18.legitmessages.model

import java.io.File

class ImageMessage(sender: String, val file: File) : Message(sender, "") {

    override fun send(chatId: String) {
        val db = Database()

        db.addImageMessage(chatId, this)
    }
}