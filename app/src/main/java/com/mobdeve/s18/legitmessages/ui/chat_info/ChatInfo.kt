package com.mobdeve.s18.legitmessages.ui.chat_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityChatInfoBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity
import com.mobdeve.s18.legitmessages.ui.create_group.SelectGroupAdapter
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var selectAdapter: SelectGroupAdapter
private lateinit var binding: ActivityChatInfoBinding
private lateinit var linearLayoutManager: LinearLayoutManager
private var chatId: String = ""
val db = Database()

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


        binding.remove.setOnClickListener {
            val selectedUsers: ArrayList<String> = selectAdapter.getSelected()
            if (selectedUsers.size == 0)
                Toast.makeText(applicationContext,"Please select a user", Toast.LENGTH_LONG).show()
            else {
                Log.i("Remove", "$selectedUsers")
                CoroutineScope(Dispatchers.Main).launch {
                    db.removeParticipant(chatId, selectedUsers)
                }
                finish()
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

    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.Main).launch {
            val participants = db.getParticipants(chatId)

            selectAdapter = SelectGroupAdapter(participants)
            binding.rvParticipants.adapter = selectAdapter

            if(participants.size <= 2){
                binding.btnLayout.visibility = View.GONE
            }
        }
    }
}