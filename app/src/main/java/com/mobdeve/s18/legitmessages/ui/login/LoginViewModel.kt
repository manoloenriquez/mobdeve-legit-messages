package com.mobdeve.s18.legitmessages.ui.login

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.User

class LoginViewModel(private val activity: LoginActivity) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val auth: FirebaseAuth = Firebase.auth

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        var result = false
        var user: User? = null
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "sign in successful")
                }
                user = User(auth.currentUser?.uid, auth.currentUser?.email, auth.currentUser?.displayName)
                result = true
                user!!.uid?.let { Log.i("Testing", it) }
            }


        if (result) {
            _loginResult.value =
                LoginResult(success = user?.let { LoggedInUserView(displayName = it.displayName) })
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}