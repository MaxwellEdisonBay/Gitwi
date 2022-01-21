package com.mangofriends.mangoappnewest.presentation.input


class LongTextFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorMessage = ::usernameErrorMessage,
    maxChars = Input.MAX_BIO_CHARS
)

private fun isUsernameValid(username: String): Boolean {
    return true
}

private fun usernameErrorMessage(username: String) = "Text $username is invalid"