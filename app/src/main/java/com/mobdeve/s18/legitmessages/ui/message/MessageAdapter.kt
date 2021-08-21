package com.mobdeve.s18.legitmessages.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.Chat

class MessageAdapter(private val list: ArrayList<Chat>):
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    public final var MSG_TYPE_LEFT = 0
    public final var MSG_TYPE_RIGHT = 1

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageBox: TextView = view.findViewById(R.id.show_message)
        val timeStamp: TextView = view.findViewById(R.id.time_stamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item_right, parent, false)

        return MessageAdapter.MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.messageBox.text = list[position].message
        holder.timeStamp.text = list[position].timeStamp
    }

    override fun getItemCount() = list.size

}

