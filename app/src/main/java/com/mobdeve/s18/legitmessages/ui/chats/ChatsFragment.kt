package com.mobdeve.s18.legitmessages.ui.chats

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
            chatAdapter = ChatAdapter(db.getChats())
            binding.chatsRvList.adapter = chatAdapter
        }

    }

}