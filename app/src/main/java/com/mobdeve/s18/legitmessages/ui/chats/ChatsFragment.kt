package com.mobdeve.s18.legitmessages.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobdeve.s18.legitmessages.R

class ChatsFragment : Fragment() {

    private lateinit var chatsViewModel: ChatsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        chatsViewModel =
                ViewModelProvider(this).get(ChatsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_chats, container, false)
        val textView: TextView = root.findViewById(R.id.text_chats)
        chatsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}