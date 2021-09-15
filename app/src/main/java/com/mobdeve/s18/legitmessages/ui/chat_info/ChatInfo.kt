package com.mobdeve.s18.legitmessages.ui.chat_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityChatInfoBinding
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity
import com.mobdeve.s18.legitmessages.ui.create_group.SelectGroupAdapter
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactAdapter

private lateinit var selectAdapter: SelectGroupAdapter
private lateinit var binding: ActivityChatInfoBinding
private lateinit var linearLayoutManager: LinearLayoutManager
private var chatId: String = ""

class ChatInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatId = intent.getStringExtra("chat_id").toString()

        Toast.makeText(applicationContext, "Added user: " + intent.getStringExtra("uid"), Toast.LENGTH_SHORT).show()

        if(intent.getStringExtra("uid") != null){
            intent.action = null
        }

        binding = ActivityChatInfoBinding.inflate(layoutInflater)

        binding.chatId.text = chatId

        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.rvParticipants.layoutManager = linearLayoutManager
        var userList = ArrayList<User>()
        userList.add(User("1", "testuser@gmail.com", "Test User"))
        userList.add(User("2", "testuser@gmail.com", "Test User"))
        userList.add(User("3", "testuser@gmail.com", "Test User"))
        selectAdapter = SelectGroupAdapter(userList)
        binding.rvParticipants.adapter = selectAdapter

        if(userList.size <= 2){
            binding.btnLayout.visibility = View.GONE
        }

        binding.remove.setOnClickListener {
            var selectedUsers: ArrayList<String> = selectAdapter.getSelected() as ArrayList<String>
            if(selectedUsers.size == 0)
                Toast.makeText(applicationContext,"Please select a user", Toast.LENGTH_LONG).show()
            else{
                //remove participants from gc here
                Toast.makeText(applicationContext, "Participants: $selectedUsers", Toast.LENGTH_LONG).show()
            }
        }

        binding.add.setOnClickListener {
            val intent = Intent(applicationContext, AddContactActivity::class.java)
            intent.putExtra("chat_id", chatId)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}