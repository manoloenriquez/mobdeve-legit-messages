package com.mobdeve.s18.legitmessages.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.databinding.FragmentContactsBinding
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.util.DataHelper

class ContactsFragment : Fragment() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentContactsBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        var dataHelper = DataHelper()
        var contactList : ArrayList<User> = dataHelper.initList()

        binding = FragmentContactsBinding.inflate(layoutInflater)
        contactAdapter = ContactAdapter(contactList)
        linearLayoutManager = LinearLayoutManager(activity)

        binding.rvContactList.adapter = contactAdapter
        binding.rvContactList.layoutManager = linearLayoutManager

        return binding.root
    }
}