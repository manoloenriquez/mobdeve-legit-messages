package com.mobdeve.s18.legitmessages.ui.search_contact

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivitySearchUserBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.contacts.ContactAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class SearchUserActivity : Activity() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: ActivitySearchUserBinding
    private var contactList = ArrayList<User>()
    private val db = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactAdapter = ContactAdapter(contactList)
        binding.rvSearchContact.adapter = contactAdapter
        binding.searchUserInput.addTextChangedListener(text)
        binding.rvSearchContact.layoutManager = LinearLayoutManager(applicationContext)

    }

    private var text = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            CoroutineScope(Main).launch {
                contactList = db.searchUser(s.toString())
                contactAdapter = ContactAdapter(contactList)
                binding.rvSearchContact.adapter = contactAdapter
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

}