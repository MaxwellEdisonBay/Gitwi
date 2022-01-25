package com.mangofriends.mangoappnewest.presentation.input


class AgeFieldState : TextFieldState(
    validator = ::isValid,
    errorMessage = ::errorMessage,
    maxChars = Input.MAX_AGE_CHARS
)

private fun isValid(username: String): Boolean {
    return true
}

private fun errorMessage(username: String) = "Age $username is invalid"