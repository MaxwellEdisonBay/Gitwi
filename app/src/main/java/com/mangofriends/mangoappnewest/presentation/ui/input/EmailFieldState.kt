package com.ivanteslenko.ui.input

import com.mangofriends.mangoappnewest.presentation.ui.input.TextFieldState
import java.util.regex.Pattern

class EmailFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorMessage = ::usernameErrorMessage
)

private const val USERNAME_VALIDATION_REGEX = "[A-Za-z0-9@.+-]*"
private fun isUsernameValid(username: String): Boolean {
    return (Pattern.matches(USERNAME_VALIDATION_REGEX, username))
}

private fun usernameErrorMessage(username: String) = "Username $username is invalid"