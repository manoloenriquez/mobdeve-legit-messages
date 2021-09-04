package com.mobdeve.s18.legitmessages.ui.contacts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityContactBinding
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.displayNameContact.text = intent.getStringExtra("displayName")
        binding.emailContact.text = intent.getStringExtra("email")
        binding.usernameContact.text = intent.getStringExtra("username")

        binding.goToChat.setOnClickListener {

            val intent = Intent(applicationContext, ChatActivity::class.java)

//            intent.putExtra("chat_username", intent.getStringExtra("username"))
//            intent.putExtra("chat_displayName", intent.getStringExtra("displayName"))
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.addContact.setOnClickListener {
            Toast.makeText(applicationContext, "UID: " + intent.getStringExtra("uid"), Toast.LENGTH_SHORT).show()
        }

        binding.deleteContact.setOnClickListener {

        }


    }
}