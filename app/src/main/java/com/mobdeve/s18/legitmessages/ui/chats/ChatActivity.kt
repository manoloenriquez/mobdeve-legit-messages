package com.mobdeve.s18.legitmessages.ui.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.databinding.ActivityChatBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.message.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
//        User.currentUser?.uid?.let { Message(it,"Hello", "11:59pm") }?.let { messageList.add(it) }
        messageList.add(Message("manolo", "test message", "11:59pm"))

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
            else{
                hour -= 12
                meridian = "PM"
            }

            if(!binding.messageInput.equals("")){
                User.currentUser?.uid?.let { it1 ->
                    com.mobdeve.s18.legitmessages.model.Message(
                        it1, binding.messageInput.text.toString(),   "$hour:$minute " + meridian)
                }?.let { it2 -> messageList.add(it2) }
                setAdapter()
                binding.messageInput.text = null
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val db = Database()
        val chatId = intent.getStringExtra("uid")
        if (chatId != null) {
            Log.i("Messages", chatId)
        }
        CoroutineScope(Dispatchers.Main).launch {
            messageList = chatId?.let { db.getMessages(it) }!!
            setAdapter()
        }
    }

    fun setAdapter(){
        messageAdapter = MessageAdapter(messageList)
        binding.rvChat.adapter = messageAdapter
        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.rvChat.layoutManager = linearLayoutManager
    }
}