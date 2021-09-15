package com.mobdeve.s18.legitmessages.ui.chat_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityAddContactBinding
import com.mobdeve.s18.legitmessages.model.User

private lateinit var binding: ActivityAddContactBinding
private lateinit var linearLayoutManager: LinearLayoutManager
private lateinit var addContactAdapter: AddContactAdapter
private var chatId: String = ""

class AddContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddContactBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.rvContactList.layoutManager = linearLayoutManager
        val contactList = User.currentUser?.contacts
        addContactAdapter = contactList?.let { AddContactAdapter(it) }!!
        binding.rvContactList.adapter = addContactAdapter

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

