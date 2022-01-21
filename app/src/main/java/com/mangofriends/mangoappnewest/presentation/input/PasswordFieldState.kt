package com.mangofriends.mangoappnewest.presentation.input

import com.mangofriends.mangoappnewest.presentation.input.Input.MIN_PASSWORD_CHARS


class PasswordFieldState : TextFieldState(
    validator = ::isPasswordValid,
    errorMessage = { passwordErrorMessage() },
    maxChars = Input.MAX_PASSWORD_CHARS
)

private fun isPasswordValid(password: String) =
    password.length >= MIN_PASSWORD_CHARS || password.isEmpty()

private fun passwordErrorMessage() = "Password must be at least $MIN_PASSWORD_CHARS characters"