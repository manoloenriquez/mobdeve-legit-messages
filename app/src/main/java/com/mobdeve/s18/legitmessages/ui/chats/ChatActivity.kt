package com.mobdeve.s18.legitmessages.ui.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.databinding.ActivityChatBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.message.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList = ArrayList()
        setAdapter()

        binding.chatHeader.text = intent.getStringExtra("chat_displayName")

        initSendMessage()
        initListener()
        setSupportActionBar(binding.chatToolBar)

    }

//    override fun onStart() {
//        super.onStart()
//
//        val db = Database()
//        val chatId = intent.getStringExtra("uid")
//        if (chatId != null) {
//            Log.i("Messages", chatId)
//        }
//        CoroutineScope(Dispatchers.Main).launch {
//            messageList = chatId?.let { db.getMessages(it) }!!
//            setAdapter()
//        }
//    }

    fun setAdapter(){
        messageAdapter = MessageAdapter(messageList)
        binding.rvChat.adapter = messageAdapter
        linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd
        binding.rvChat.layoutManager = linearLayoutManager
        binding.rvChat.smoothScrollToPosition(messageList.size)
    }

    fun initSendMessage() {
        binding.messageSend.setOnClickListener {
            if(!binding.messageInput.equals("")){
                val message = User.currentUser?.uid?.let { it1 ->
                    Message(
                        it1,
                        binding.messageInput.text.toString()
                    )
                }

                if (message != null) {
//                    messageList.add(message)
                    intent.getStringExtra("uid")?.let { it1 -> message.send(it1) }
                }
                setAdapter()
                binding.messageInput.text = null
            }
        }
    }

    fun initListener() {
        val chatId: String? = intent.getStringExtra("uid")

        val ref = chatId?.let {
                    Firebase.firestore
                        .collection("chats")
                        .document(it)
                        .collection("messages")
                        .orderBy("timeStamp")
                }



        Log.i("ChatUpdater", "$ref")

        ref?.addSnapshotListener { value, error ->
            Log.i("ChatUpdater", "Detected update")
            val messages = ArrayList<Message>()
            CoroutineScope(Dispatchers.Main).launch {
                value?.documents?.forEach { document ->
                    withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                        val data = document.data
                        val sender = (data?.get("sender") as DocumentReference).id

                        val timeStamp = data.get("timeStamp") as Timestamp

                        val message = Message(
                            sender,
                            data.get("message") as String,
                            timeStamp
                        )

                        messages.add(message)
                    }
                }
                Log.i("ChatUpdater", "$messages")
                messageList = messages
                setAdapter()
            }
        }
    }
}