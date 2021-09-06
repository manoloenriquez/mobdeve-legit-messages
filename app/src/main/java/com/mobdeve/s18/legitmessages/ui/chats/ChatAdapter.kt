package com.mobdeve.s18.legitmessages.ui.chats

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.Chat
import com.mobdeve.s18.legitmessages.ui.contacts.ContactActivity

class ChatAdapter(private val list: ArrayList<Chat>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        lateinit var uid: String
        val username: TextView = view.findViewById(R.id.chat_username)
        val chatData: ImageView = view.findViewById(R.id.chatData)
        lateinit var chat_username: String

        init{
            chatData.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                val intent = Intent(view.context, ChatActivity::class.java)

                intent.putExtra("uid", uid)
                intent.putExtra("chat_username", chat_username )
                intent.putExtra("chat_displayName", username.text)
                v.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_data, parent, false)

        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        holder.username.text = list[position].username
        holder.chat_username = list[position].username
        holder.uid = list[position].chatId.toString()
    }

    override fun getItemCount() = list.size

}