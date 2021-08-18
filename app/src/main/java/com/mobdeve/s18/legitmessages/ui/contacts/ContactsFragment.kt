package com.mobdeve.s18.legitmessages.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.adapter.ContactAdapter
import com.mobdeve.s18.legitmessages.databinding.FragmentContactsBinding
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.util.DataHelper

class ContactsFragment : Fragment() {

    private var dataHelper: DataHelper = DataHelper()
    private var contactList: ArrayList<User> = arrayListOf()
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentContactsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts,
            container, false)
        contactList = dataHelper.initList()
        contactAdapter = ContactAdapter(contactList)
        binding.rvContactList.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        binding.rvContactList.adapter = contactAdapter
        binding.text.text = "Hello " + contactList.get(0).username
        return binding.root


    }

    override fun onPause() {
        super.onPause()
        Log.i("DATA", contactList.toString())
    }
}