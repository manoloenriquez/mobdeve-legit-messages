package com.mobdeve.s18.legitmessages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.User
import android.widget.TextView
import com.mobdeve.s18.legitmessages.ui.contacts.ContactsFragment


class ContactAdapter():
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val contactArrayList: ArrayList<User> = ArrayList<User>()
    private lateinit var context: Context
    private var names = arrayOf("Jolo", "Manolo")

    override fun getItemCount(): Int {
        return contactArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_data, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactAdapter.ContactViewHolder, position: Int) {
        holder.username.text = names[position]
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.contact_username)

    }

}