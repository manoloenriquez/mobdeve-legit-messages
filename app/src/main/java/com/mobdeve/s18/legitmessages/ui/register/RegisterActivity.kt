package com.mobdeve.s18.legitmessages.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usernameInput.addTextChangedListener(textWatcher)
        binding.firstNameInput.addTextChangedListener(textWatcher)
        binding.lastNameInput.addTextChangedListener(textWatcher)
        binding.emailInput.addTextChangedListener(textWatcher)
        binding.passwordInput.addTextChangedListener(textWatcher)


        binding.registerBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Username: " + binding.usernameInput.text, Toast.LENGTH_SHORT).show()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val username = binding.usernameInput.text.trim()
            val firstName = binding.firstNameInput.text.trim()
            val lastName = binding.lastNameInput.text.trim()
            val email = binding.emailInput.text.trim()
            val password = binding.passwordInput.text.trim()

            if(username.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
                binding.registerBtn.isEnabled = true

        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}