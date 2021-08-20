package com.mobdeve.s18.legitmessages.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityContactBinding

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

        }


    }
}