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
    private lateinit var binding: FragmentContactsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactsBinding.inflate(layoutInflater)
        contactList = dataHelper.initList()
        contactAdapter = ContactAdapter()
        binding.rvContactList.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        binding.rvContactList.adapter = contactAdapter

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.i("DATA", contactList.toString())
    }
}