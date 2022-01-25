package com.mangofriends.mangoappnewest.presentation.input

import java.util.regex.Pattern

class EmailFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorMessage = ::usernameErrorMessage,
    maxChars = Input.MAX_EMAIL_CHARS
)

private const val VALIDATION_REGEX = "[A-Za-z0-9@.+-]*"
private fun isUsernameValid(username: String): Boolean {
    return (Pattern.matches(VALIDATION_REGEX, username))
}

private fun usernameErrorMessage(username: String) = "Email $username is invalid"