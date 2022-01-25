package com.mangofriends.mangoappnewest.presentation.input

import java.util.regex.Pattern

class StandardNameFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorMessage = ::usernameErrorMessage,
    maxChars = Input.MAX_STANDARD_NAME_CHARS
)


private const val VALIDATION_REGEX = "[A-Za-z0-9À-ž]*"
private fun isUsernameValid(username: String): Boolean {
    return (Pattern.matches(VALIDATION_REGEX, username))
}

private fun usernameErrorMessage(username: String) = "Value $username is invalid"