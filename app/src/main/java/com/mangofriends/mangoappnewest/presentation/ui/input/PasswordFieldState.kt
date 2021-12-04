package com.mangofriends.mangoappnewest.presentation.ui.input

import com.mangofriends.mangoappnewest.common.Constants.MIN_PASSWORD_CHARS

class PasswordFieldState : TextFieldState(
    validator = ::isPasswordValid,
    errorMessage = { passwordErrorMessage() }
)

fun isPasswordValid(password: String) = password.length >= MIN_PASSWORD_CHARS || password.isEmpty()

fun passwordErrorMessage() = "Password must be at least $MIN_PASSWORD_CHARS characters"