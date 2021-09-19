package com.mobdeve.s18.legitmessages.model

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageMessage(sender: String, val uri: Uri) : Message(sender, "") {

    override fun send(chatId: String) {
        val db = Database()
        val instance = this

        CoroutineScope(Dispatchers.Main).launch {
            db.addImageMessage(chatId, instance)
        }
    }
}