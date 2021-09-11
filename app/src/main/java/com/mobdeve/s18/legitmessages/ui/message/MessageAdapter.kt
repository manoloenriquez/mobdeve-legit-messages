package com.mobdeve.s18.legitmessages.ui.message

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactActivity

class MessageAdapter(private val list: ArrayList<Message>):
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    final var MSG_TYPE_LEFT = 0
    final var MSG_TYPE_RIGHT = 1

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val message: LinearLayout = view.findViewById(R.id.message_layout)
        val messageBox: TextView = view.findViewById(R.id.show_message)
        val timeStamp: TextView = view.findViewById(R.id.time_stamp)

        val db = Database()

        init {
            message.setOnLongClickListener{
                val popup = PopupMenu(view.context, message)
                popup.menuInflater.inflate(R.menu.chat_menu, popup.menu)

                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.edit_message -> {
                            Toast.makeText(view.context, "Edit text", Toast.LENGTH_SHORT).show()
                            Log.i("Edit Messge", "editing...")
                        }

                        R.id.delete_message -> {
//                            db.deleteMessage()
                        }
                    }
                    true
                })
                popup.show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_right, parent, false)
            MessageAdapter.MessageViewHolder(view)

        } else{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_left, parent, false)
            MessageAdapter.MessageViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.messageBox.text = list[position].message
        holder.timeStamp.text = list[position].timeStampString()
    }

    override fun getItemViewType(position: Int): Int{
        if(list[position].sender == User.currentUser?.uid.toString())
            return MSG_TYPE_RIGHT
        else
            return MSG_TYPE_LEFT
    }

    override fun getItemCount() = list.size

}

