package com.mobdeve.s18.legitmessages.ui.chats

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.FirebaseStorage
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.FragmentChatsBinding
import com.mobdeve.s18.legitmessages.databinding.SelectGroupContactBinding
import com.mobdeve.s18.legitmessages.model.Chat
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.contacts.ContactAdapter
import com.mobdeve.s18.legitmessages.ui.create_group.SelectGroupActivity
import com.mobdeve.s18.legitmessages.ui.search_contact.SearchUserActivity
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val chats: ArrayList<Chat> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        binding = FragmentChatsBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(activity)
        binding.chatsRvList.layoutManager = linearLayoutManager

        chatAdapter = ChatAdapter(chats)
        binding.chatsRvList.adapter = chatAdapter

        initChatListener()

        binding.seachUser.setOnClickListener {

            val popup = PopupMenu(activity, binding.seachUser)
            popup.menuInflater.inflate(R.menu.select_chat_menu, popup.menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.new_chat -> {
                        val intent = Intent(activity, SelectContactActivity::class.java)
                        startActivity(intent)
                        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    R.id.new_group_chat ->{
                        val intent = Intent(activity, SelectGroupActivity::class.java)
                        startActivity(intent)
                        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                true
            })
            popup.show()
        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val db = Database()

        CoroutineScope(Dispatchers.Main).launch {
//            chatAdapter = ChatAdapter(db.getChats())
//            binding.chatsRvList.adapter = chatAdapter
            var chatList = db.getChats()
            for(i in chatList){
                Firebase.messaging.subscribeToTopic(i.chatId)
                    .addOnCompleteListener { task ->
                        var msg = i.chatId
                        if (!task.isSuccessful) {
                            msg = getString(R.string.msg_subscribe_failed)
                        }
                        Log.d("Topic", msg)
                    }
            }
        }

    }

    fun initChatListener() {
        val db = Firebase.firestore

        val currentUid: String? = User.currentUser!!.uid
//        val chats = ArrayList<Chat>()
        val userRef: DocumentReference? = currentUid?.let { db.collection("users").document(it) }
        val receivers = hashMapOf<String, String>()

        if (userRef != null) {
            val chatsRef = db.collection("chats")
                .whereArrayContains("participants", userRef)
                .limit(100)
//                .get()
//                .await()

            chatsRef.addSnapshotListener { snapshot, error ->
                CoroutineScope(Dispatchers.Main).launch {
                    snapshot?.documents?.forEach { document ->

                            val data = document.data
                            val chat = Chat(document.id)
                            Log.i("Chat id", chat.chatId)

                            (data?.get("participants") as ArrayList<DocumentReference>).forEach { userRef ->
                                withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                                    if (receivers.get(userRef.id) != null) {
                                        chat.participants.add(receivers.get(userRef.id)!!)
                                    } else if (userRef.id != currentUid) {
                                        val user = userRef.get().await()
                                        val username: String = user.data?.get("username") as String
                                        receivers.put(userRef.id, username)

                                        chat.participants.add(receivers.get(userRef.id)!!)
                                    }
                                }
                            }

                            chat.label = chat.usernamesString()
                            Log.d("Chats", chat.label)
                            chats.add(chat)

                        }
//                    chatAdapter = ChatAdapter(chats)
//                    binding.chatsRvList.adapter = chatAdapter

                    chatAdapter.notifyDataSetChanged()
                    Log.d("Chats", "$chats")
                }
            }

        }
    }
}