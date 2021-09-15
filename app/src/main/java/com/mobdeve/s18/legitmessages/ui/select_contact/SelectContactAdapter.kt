package com.mobdeve.s18.legitmessages.ui.select_contact

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectContactAdapter(private val list: ArrayList<User>): RecyclerView.Adapter<SelectContactAdapter.SelectContactViewHolder>() {

    class SelectContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.contact_username)
        val displayName: TextView = view.findViewById(R.id.contact_display_name)
        val contactData: ImageView = view.findViewById(R.id.contactData)

        lateinit var email: String
        lateinit var userName: String
        lateinit var uid: String

        init {
            val db = Database()

            contactData.setOnClickListener { v: View ->
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(view.context, ChatActivity::class.java)



                    // already creates a message thread in firebase when you send a message, pero no participants lang
                    // checks if message thread already exists with this person else make new thread

                    val participants: ArrayList<String> = ArrayList()
                    User.currentUser?.uid?.let { participants.add(it) }
                    participants.add(uid)

                    val chatId = db.createChat(participants)

                    intent.putExtra("uid", chatId)
                    intent.putExtra("chat_displayName", username.text)

                    v.context.startActivity(intent)
                }


            }
        }

        fun checkChatExists(): Boolean {
            val chats = User.currentUser?.chats



            return false
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