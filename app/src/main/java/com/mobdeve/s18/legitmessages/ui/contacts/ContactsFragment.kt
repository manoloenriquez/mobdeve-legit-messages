package com.mobdeve.s18.legitmessages.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.adapter.ContactAdapter
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.util.DataHelper

class ContactsFragment : Fragment() {

    private var dataHelper: DataHelper = DataHelper()
    private var contactList: ArrayList<User> = ArrayList<User>()
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var rv_contacts: RecyclerView
    private lateinit var binding: ContactsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts,container, false)
        contactList = dataHelper.initList()
        contactAdapter = activity?.let { ContactAdapter(contactList, it.applicationContext) }!!



        return binding.root
    }

//    override fun onPause() {
//        super.onPause()
//        Log.i("DATA", contactList.toString())
//    }
}