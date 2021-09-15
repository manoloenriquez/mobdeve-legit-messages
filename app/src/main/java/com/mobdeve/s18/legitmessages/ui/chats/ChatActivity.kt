package com.mobdeve.s18.legitmessages.ui.chats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityChatBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chat_info.ChatInfo
import com.mobdeve.s18.legitmessages.ui.message.MessageAdapter
import com.mobdeve.s18.legitmessages.ui.search_chat.SearchChatActivity
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactActivity
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
    private lateinit var chat_id: String
    private var disappear: Boolean = false
    val db = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chat_id = intent.getStringExtra("uid").toString()
        messageList = ArrayList()
        setAdapter()

        binding.chatHeader.text = intent.getStringExtra("chat_displayName")

        binding.attachFile.setOnClickListener {

            val popup = PopupMenu(applicationContext, binding.attachFile)
            popup.menuInflater.inflate(R.menu.chat_options, popup.menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.photo_video ->
                        Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()

                    R.id.disappear ->{
                        if(!disappear){
                            disappear = true
                            Toast.makeText(applicationContext, "Disappear is on", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            disappear = false
                            Toast.makeText(applicationContext, "Disappear is off", Toast.LENGTH_SHORT).show()
                        }
                    }
                    R.id.drawing ->
                        Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()

                    R.id.text_to_speech ->
                        Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
                }
                true
            })
            popup.show()
        }

        binding.chatInfoBtn.setOnClickListener {

            val popup = PopupMenu(applicationContext, binding.chatInfoBtn)
            popup.menuInflater.inflate(R.menu.chat_info, popup.menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.search_chat -> {
                        val intent = Intent(applicationContext, SearchChatActivity::class.java)
                        intent.putExtra("chat_id", chat_id)

                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    R.id.chat_info ->{
                        val intent = Intent(applicationContext, ChatInfo::class.java)
                        intent.putExtra("chat_id", chat_id)

                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                true
            })
            popup.show()
        }

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
                if(disappear){
                    Handler().postDelayed({
                        if (message != null) {
                            intent.getStringExtra("uid")?.let { it1 -> db.deleteMessage(it1, message.id) }
                        }
                        setAdapter()
                    }, 10000)
                }
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
                            timeStamp,
                            document.id,
                            chatId
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