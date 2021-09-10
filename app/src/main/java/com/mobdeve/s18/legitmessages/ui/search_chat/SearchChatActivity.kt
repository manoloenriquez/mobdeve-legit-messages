package com.mobdeve.s18.legitmessages.ui.search_chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivitySearchChatBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.contacts.ContactAdapter
import com.mobdeve.s18.legitmessages.ui.message.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var messageAdapter: MessageAdapter
private lateinit var binding: ActivitySearchChatBinding
private var messageList = ArrayList<Message>()
private val db = Database()

class SearchChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchChatBinding.inflate(layoutInflater)
        binding.searchChatInput.addTextChangedListener(text)
        setContentView(binding.root)

        messageAdapter = MessageAdapter(messageList)
        binding.searchChatInput.addTextChangedListener(text)
        binding.rvSearchChat.layoutManager = LinearLayoutManager(applicationContext)

    }
}

private var text = object: TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        
        CoroutineScope(Dispatchers.Main).launch {

        }
    }

    override fun afterTextChanged(s: Editable?) {

    }
}