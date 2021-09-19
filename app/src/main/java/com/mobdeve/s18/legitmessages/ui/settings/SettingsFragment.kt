package com.mobdeve.s18.legitmessages.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.MainActivity
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.FragmentSettingsBinding
import com.mobdeve.s18.legitmessages.model.User
import com.mobdeve.s18.legitmessages.ui.login.LoginActivity
import java.lang.RuntimeException

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)
        val binding = FragmentSettingsBinding.inflate(layoutInflater)

        binding.logout.setOnClickListener {
            Toast.makeText(it.context, "Logged out!", Toast.LENGTH_SHORT).show()
            Log.i("Settings", "Logged out!")
            Firebase.auth.signOut()
            val intent = Intent(it.context, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.crash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

        binding.firstName.setText(User.currentUser?.firstName)
        binding.lastName.setText(User.currentUser?.lastName)
        binding.username.setText(User.currentUser?.username)


        return binding.root
    }

}