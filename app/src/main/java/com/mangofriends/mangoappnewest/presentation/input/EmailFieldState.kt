package com.mangofriends.mangoappnewest.presentation.input

import android.util.Patterns

class EmailFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorMessage = ::usernameErrorMessage,
    maxChars = Input.MAX_EMAIL_CHARS
)

private const val VALIDATION_REGEX = "[A-Za-z0-9@.+-]*"
private fun isUsernameValid(email: String): Boolean {
//    return (Pattern.matches(VALIDATION_REGEX, username))
    return if (email.isNotEmpty())
        Patterns.EMAIL_ADDRESS.matcher(email).matches();
    else
        true
}

private fun usernameErrorMessage(username: String) = "Email $username is invalid"