package com.mobdeve.s18.legitmessages.ui.login

import com.mobdeve.s18.legitmessages.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    val error: Int? = null
)