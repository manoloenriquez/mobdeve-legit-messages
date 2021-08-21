package com.mobdeve.s18.legitmessages.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.FragmentContactsBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.ui.search_contact.SearchUserActivity
import com.mobdeve.s18.legitmessages.util.DataHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class ContactsFragment : Fragment() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentContactsBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val db = Database()
        val currentUser = Firebase.auth.currentUser
        binding = FragmentContactsBinding.inflate(layoutInflater)

        var dataHelper = DataHelper()
        linearLayoutManager = LinearLayoutManager(activity)
        binding.rvContactList.layoutManager = linearLayoutManager

        CoroutineScope(Main).launch {
            val contactList = currentUser?.let { db.getUserContacts(it.uid) }
            contactAdapter = contactList?.let { ContactAdapter(it) }!!

            binding.rvContactList.adapter = contactAdapter
        }

        binding.seachUser.setOnClickListener {
            var intent = Intent(activity, SearchUserActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        return binding.root
    }
}