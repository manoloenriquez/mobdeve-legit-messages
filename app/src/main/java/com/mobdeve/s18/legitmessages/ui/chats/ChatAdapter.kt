package com.mobdeve.s18.legitmessages.ui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R

class ChatAdapter(private val list: ArrayList<String>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val username: TextView

        init{
            username = view.findViewById(R.id.chat_username)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_data, parent, false)

        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        holder.username.text = list[position]
    }

    override fun getItemCount() = list.size

}