package com.mobdeve.s18.legitmessages.ui.create_group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.User

class SelectGroupAdapter(private val list: ArrayList<User>): RecyclerView.Adapter<SelectGroupAdapter.SelectGroupViewHolder>() {

    private var selectedUsers: ArrayList<String> = ArrayList()

    init {
        User.currentUser?.uid?.let { selectedUsers.add(it) }
    }

    class SelectGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.contact_username)
        val displayName: TextView = view.findViewById(R.id.contact_display_name)
        val checkbox: CheckBox = view.findViewById(R.id.checkbox)

        lateinit var email: String
        lateinit var userName: String
        lateinit var uid: String

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectGroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_group_contact, parent, false)

        return SelectGroupViewHolder(view)
    }

    fun getSelected(): ArrayList<String>{
        return selectedUsers
    }

    override fun onBindViewHolder(holder: SelectGroupViewHolder, position: Int) {
        holder.username.text = list[position].username
        holder.displayName.text = list[position].displayName
        holder.email = list[position].email.toString()
        holder.uid = list[position].uid.toString()
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                selectedUsers.add(holder.uid)
            else
                selectedUsers.remove(holder.uid)
        }
    }

    override fun getItemCount() = list.size

}