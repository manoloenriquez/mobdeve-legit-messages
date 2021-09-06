package com.mobdeve.s18.legitmessages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            val auth = Firebase.auth
            val db = Database()
            if (auth.currentUser != null) {
//            CoroutineScope(Dispatchers.Main).launch {
                val userRef = db.getUser(auth.currentUser!!.uid)
                val data = userRef.data

                if (data != null) {
                    User.currentUser = User(
                        auth.currentUser!!.uid,
                        auth.currentUser!!.email,
                        auth.currentUser!!.displayName,
                        data["firstName"] as String,
                        data["lastName"] as String,
                        data["username"] as String
                    )

                    db.loadContacts()
//                    db.loadChats()
                }

                Log.i("Current User", "${User.currentUser}")
//            }
            }

            setContentView(R.layout.activity_main)
            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                )
            )
//        setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

        }
    }
}