package com.mobdeve.s18.legitmessages.ui.chats

import android.os.Bundle
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

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        var list = ArrayList<String>()
        list.add("Jolo")
        list.add("Manolo")

        chatAdapter = ChatAdapter(list)
        binding = FragmentChatsBinding.inflate(layoutInflater)
        binding.chatsRvList.adapter = chatAdapter
        linearLayoutManager = LinearLayoutManager(activity)
        binding.chatsRvList.layoutManager = linearLayoutManager


        return binding.root
    }
}