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
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivitySearchUserBinding
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.contacts.ContactAdapter
import org.w3c.dom.Text

class SearchUserActivity : Activity() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: ActivitySearchUserBinding
    private val contactList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactAdapter = ContactAdapter(contactList)
        binding.rvSearchContact.adapter = contactAdapter
        binding.searchUserInput.addTextChangedListener(text)


    }

    private var text = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //key up function for search bar to show matching contact usernames/displaynames

            //update the context of adapter after finding matching users
                contactAdapter = ContactAdapter(contactList)
                binding.rvSearchContact.adapter = contactAdapter

                Toast.makeText(applicationContext, binding.searchUserInput.text, Toast.LENGTH_SHORT).show()
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

}