package com.mobdeve.s18.legitmessages.ui.search_chat

import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.ui.contacts.ContactActivity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import android.widget.TextView

class SearchChatAdapter(private val list: ArrayList<Message>): RecyclerView.Adapter<SearchChatAdapter.SearchAdapterViewHolder>() {

    class SearchAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.chat_username)
        val message: TextView = view.findViewById(R.id.chat_message)
        val date: TextView = view.findViewById(R.id.chat_date)

        lateinit var email: String
        lateinit var userName: String
        lateinit var uid: String
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_chat_data, parent, false)

        return SearchAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder, position: Int) {
        holder.username.text = list[position].sender
        holder.message.text = list[position].message
        holder.date.text = list[position].timeStampString()
    }

    override fun getItemCount() = list.size

}