package com.mobdeve.s18.legitmessages.ui.chat_info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.mobdeve.s18.legitmessages.databinding.ActivityAddContactBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class AddContactAdapter(private val list: ArrayList<User>, chatId: String): RecyclerView.Adapter<AddContactAdapter.AddContactViewHolder>() {
    var chatId: String = chatId

    class AddContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.contact_username)
        val displayName: TextView = view.findViewById(R.id.contact_display_name)
        val contactData: ImageView = view.findViewById(R.id.contactData)

        lateinit var email: String
        lateinit var userName: String
        lateinit var uid: String

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_data, parent, false)

        return AddContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddContactViewHolder, position: Int) {
        holder.username.text = list[position].username
        holder.displayName.text = list[position].displayName
        holder.email = list[position].email.toString()
        holder.uid = list[position].uid.toString()

        holder.contactData.setOnClickListener { v: View ->

            // add user to participants list here
            // chatId is already in parameters
            CoroutineScope(Dispatchers.Main).launch {
                db.addParticipant(chatId, holder.uid)

                val intent = Intent(v.context, ChatInfo::class.java)
                intent.putExtra("uid", holder.uid)
                intent.putExtra("chat_id", chatId)
                v.context.startActivity(intent)
                (v.context as Activity).finish()
            }
        }
    }

    override fun getItemCount() = list.size

}