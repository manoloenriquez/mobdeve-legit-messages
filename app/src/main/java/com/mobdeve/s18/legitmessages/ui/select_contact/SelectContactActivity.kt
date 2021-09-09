package com.mobdeve.s18.legitmessages.ui.select_contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.databinding.ActivitySelectContactBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.contacts.SelectContactAdapter

private lateinit var selectAdapter: SelectContactAdapter
private lateinit var binding: ActivitySelectContactBinding
private lateinit var linearLayoutManager: LinearLayoutManager
private var contactList = ArrayList<User>()

class SelectContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectContactBinding.inflate(layoutInflater)

        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.rvContactList.layoutManager = linearLayoutManager
        val contactList = User.currentUser?.contacts
        selectAdapter = contactList?.let { SelectContactAdapter(it) }!!
        binding.rvContactList.adapter = selectAdapter

        setContentView(binding.root)
    }
}