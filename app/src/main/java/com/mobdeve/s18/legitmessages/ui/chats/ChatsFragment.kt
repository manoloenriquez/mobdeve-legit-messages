package com.mobdeve.s18.legitmessages.ui.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.FragmentChatsBinding
import com.mobdeve.s18.legitmessages.model.Chat
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.contacts.ContactAdapter
import com.mobdeve.s18.legitmessages.util.DataHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        binding = FragmentChatsBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(activity)
        binding.chatsRvList.layoutManager = linearLayoutManager
        
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val db = Database()

        CoroutineScope(Dispatchers.Main).launch {
            chatAdapter = ChatAdapter(db.getChats())
            binding.chatsRvList.adapter = chatAdapter
        }

    }

}