package com.mobdeve.s18.legitmessages.ui.register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.MainActivity
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityRegisterBinding
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var auth: FirebaseAuth = Firebase.auth
    private val db: Database = Database()

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
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val firstName = binding.firstNameInput.text.toString()
            val lastName = binding.lastNameInput.text.toString()
            val username = binding.usernameInput.text.toString()

            Toast.makeText(applicationContext, "Username: " + binding.usernameInput.text, Toast.LENGTH_SHORT).show()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = User(
                            auth.currentUser?.uid,
                            email,
                            "$firstName $lastName",
                            firstName,
                            lastName,
                            username
                        )
                        db.setUser(user)
                        val intent = Intent(this, MainActivity::class.java)
                        setResult(Activity.RESULT_OK)
                        startActivity(intent)
                        finish()
                    }
                }
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