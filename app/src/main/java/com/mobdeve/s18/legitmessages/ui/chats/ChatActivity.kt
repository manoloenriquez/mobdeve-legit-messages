package com.mobdeve.s18.legitmessages.ui.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(applicationContext, intent.getStringExtra("chat_username"), Toast.LENGTH_SHORT).show()
        binding.chatHeader.text = intent.getStringExtra("chat_displayName")
    }
}