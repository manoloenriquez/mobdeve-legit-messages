package com.mobdeve.s18.legitmessages.ui.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.databinding.ActivityChatBinding
import com.mobdeve.s18.legitmessages.ui.message.MessageAdapter
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var messageList: ArrayList<com.mobdeve.s18.legitmessages.model.Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageList = ArrayList()

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageAdapter = MessageAdapter(messageList)
        setAdapter()

        binding.chatHeader.text = intent.getStringExtra("chat_displayName")

        binding.messageSend.setOnClickListener {
            val timeNow: Calendar = Calendar.getInstance()
            var hour: Int = timeNow.get(Calendar.HOUR_OF_DAY)
            val minute: Int = timeNow.get(Calendar.MINUTE)
            val meridian: String

            if ((hour >=0) && (hour <= 11))
                meridian = "AM"
            else
                meridian = "PM"

            hour -= 12

            if(!binding.messageInput.equals("")){
                messageList.add(com.mobdeve.s18.legitmessages.model.Message(binding.messageInput.text.toString(),   "$hour:$minute " + meridian))
                setAdapter()
                binding.messageInput.text = null
            }
        }
    }

    fun setAdapter(){
        messageAdapter = MessageAdapter(messageList)
        binding.rvChat.adapter = messageAdapter
        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.rvChat.layoutManager = linearLayoutManager
    }
}