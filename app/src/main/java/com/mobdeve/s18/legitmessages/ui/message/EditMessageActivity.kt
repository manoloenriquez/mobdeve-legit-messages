package com.mobdeve.s18.legitmessages.ui.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityChatBinding
import com.mobdeve.s18.legitmessages.databinding.ActivityEditMessageBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity
import java.util.*

class EditMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMessageBinding
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editInput.setText(intent.getStringExtra("message").toString())


        binding.saveBtn.setOnClickListener {
            val db = Database()
            val chatId = intent.getStringExtra("chatId").toString()
            val messageId = intent.getStringExtra("id").toString()
            val message = binding.editInput.text.toString()

            db.editMessage(chatId, messageId, message)

            finish()
        }
    }
}