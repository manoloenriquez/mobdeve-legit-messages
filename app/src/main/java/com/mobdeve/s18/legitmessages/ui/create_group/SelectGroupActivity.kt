package com.mobdeve.s18.legitmessages.ui.create_group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.databinding.ActivitySelectGroupBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactAdapter

private lateinit var binding: ActivitySelectGroupBinding
private lateinit var linearLayoutManager: LinearLayoutManager
private var contactList = ArrayList<User>()
private lateinit var selectGroup: SelectGroupAdapter
val db = Database()

class SelectGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectGroupBinding.inflate(layoutInflater)

        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.rvContactList.layoutManager = linearLayoutManager
        val contactList = User.currentUser?.contacts
        selectGroup = contactList?.let { SelectGroupAdapter(it) }!!
        binding.rvContactList.adapter = selectGroup

        binding.createGroup.setOnClickListener {
            var selectedUsers: ArrayList<String> = selectGroup.getSelected()
            if(selectedUsers.size < 2)
                Toast.makeText(applicationContext,"Please select more participants", Toast.LENGTH_LONG).show()
            else{
                db.addChat(selectedUsers)
                finish()
            }
        }

        setContentView(binding.root)
    }
}