package com.mobdeve.s18.legitmessages.ui.select_contact

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import android.widget.TextView
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity

class SelectContactAdapter(private val list: ArrayList<User>): RecyclerView.Adapter<SelectContactAdapter.SelectContactViewHolder>() {

    class SelectContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.contact_username)
        val displayName: TextView = view.findViewById(R.id.contact_display_name)
        val contactData: ImageView = view.findViewById(R.id.contactData)

        lateinit var email: String
        lateinit var userName: String
        lateinit var uid: String

        init {
            contactData.setOnClickListener { v: View ->
//                val position: Int = adapterPosition
                val intent = Intent(view.context, ChatActivity::class.java)

                intent.putExtra("uid", uid)
                intent.putExtra("uid", uid)
                intent.putExtra("chat_displayName", username.text)
                // already creates a message thread in firebase when you send a message, pero no participants lang
                // checks if message thread already exists with this person else make new thread

                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_data, parent, false)

        return SelectContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectContactViewHolder, position: Int) {
        holder.username.text = list[position].username
        holder.displayName.text = list[position].displayName
        holder.email = list[position].email.toString()
        holder.uid = list[position].uid.toString()
    }

    override fun getItemCount() = list.size

}