package com.mobdeve.s18.legitmessages.ui.contacts

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import android.widget.TextView
import com.mobdeve.s18.legitmessages.model.User

class ContactAdapter(private val list: ArrayList<User>): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.contact_username)
        val displayName: TextView = view.findViewById(R.id.contact_display_name)
        val contactData: ImageView = view.findViewById(R.id.contactData)

        lateinit var email: String
        lateinit var userName: String
        lateinit var uid: String

        init {
            contactData.setOnClickListener { v: View ->
                val intent = Intent(view.context, ContactActivity::class.java)

                intent.putExtra("uid", uid)
                intent.putExtra("username", username.text)
                intent.putExtra("displayName", displayName.text)
                intent.putExtra("email", email)

                v.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_data, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.username.text = list[position].username
        holder.displayName.text = list[position].displayName
        holder.email = list[position].email.toString()
        holder.uid = list[position].uid.toString()
    }

    override fun getItemCount() = list.size

}