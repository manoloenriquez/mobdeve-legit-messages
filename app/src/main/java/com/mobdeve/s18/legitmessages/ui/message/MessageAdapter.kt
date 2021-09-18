package com.mobdeve.s18.legitmessages.ui.message

import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.search_chat.SearchChatActivity
import com.mobdeve.s18.legitmessages.ui.select_contact.SelectContactActivity
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(private val list: ArrayList<Message>, private var tts: TextToSpeech):
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    final var MSG_TYPE_LEFT = 0
    final var MSG_TYPE_RIGHT = 1

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val message: LinearLayout = view.findViewById(R.id.message_layout)
        val messageBox: TextView = view.findViewById(R.id.show_message)
        val timeStamp: TextView = view.findViewById(R.id.time_stamp)

        val db = Database()
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
        val db = Database()



        if(list[position].sender == User.currentUser?.uid.toString()){
            holder.message.setOnLongClickListener { v: View ->
                val popup = PopupMenu(v.context, holder.message)
                popup.menuInflater.inflate(R.menu.chat_menu, popup.menu)


                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.edit_message -> {
                            val intent = Intent(v.context, EditMessageActivity::class.java)
                            intent.putExtra("message", holder.messageBox.text )
                            intent.putExtra("chatId", list[position].chatId)
                            intent.putExtra("id", list[position].id)
                            v.context.startActivity(intent)
                        }

                        R.id.delete_message -> {
                            db.deleteMessage(list[position].chatId, list[position].id)
                        }

                        R.id.text_to_speech -> {
                            tts = TextToSpeech(v.context, TextToSpeech.OnInitListener { status ->
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                    tts.speak(list[position].message, TextToSpeech.QUEUE_FLUSH, null, "")
                                else
                                    Toast.makeText(v.context, "ERROR", Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    true
                })
                popup.show()
                true
            }
        } else {
            holder.message.setOnLongClickListener { v ->
                Toast.makeText(v.context, "You can't modify this message.", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int{
        if(list[position].sender == User.currentUser?.uid.toString()) {
            return MSG_TYPE_RIGHT
        }
        else
            return MSG_TYPE_LEFT
    }

    override fun getItemCount() = list.size

}

