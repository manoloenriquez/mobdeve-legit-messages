package com.mobdeve.s18.legitmessages.ui.contacts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.MainActivity
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityContactBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.chats.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding
    val db: Database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.displayNameContact.text = intent.getStringExtra("displayName")
        binding.emailContact.text = intent.getStringExtra("email")
        binding.usernameContact.text = intent.getStringExtra("username")

        binding.addContact.setOnClickListener {
            val contactUid: String = intent.getStringExtra("uid") ?: return@setOnClickListener

            CoroutineScope(Dispatchers.Main).launch {
                if (User.currentUser?.uid?.let { it1 -> db.addContact(it1, contactUid) } == true) {
                    Toast.makeText(applicationContext, "Successfully added", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.deleteContact.visibility = View.GONE
        binding.deleteContact.setOnClickListener {
            val contactUid: String = intent.getStringExtra("uid") ?: return@setOnClickListener
            Log.i("ContactUid", contactUid)
            CoroutineScope(Dispatchers.Main).launch {
                if (User.currentUser?.uid?.let { it1 -> db.deleteContact(it1, contactUid) } == true) {
                    Toast.makeText(applicationContext, "Successfully deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val contactId = intent.getStringExtra("uid")

        User.currentUser?.contacts?.forEach { user ->
            if (user.uid == contactId) {
                binding.addContact.visibility = View.GONE
                binding.deleteContact.visibility = View.VISIBLE
            }
        }
    }
}