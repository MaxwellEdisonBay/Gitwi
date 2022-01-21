package com.mangofriends.mangoappnewest.presentation.input

import java.util.regex.Pattern

class ShortASCIIFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorMessage = ::usernameErrorMessage,
    maxChars = Input.MAX_SHORT_ASCII_CHARS
)


private const val VALIDATION_REGEX = "[A-Za-zÀ-ž ]*"
private fun isUsernameValid(username: String): Boolean {
    return (Pattern.matches(VALIDATION_REGEX, username))
}

private fun usernameErrorMessage(username: String) = "Value $username is invalid"