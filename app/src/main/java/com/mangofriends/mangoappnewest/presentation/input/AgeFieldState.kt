package com.mangofriends.mangoappnewest.presentation.input

import java.util.regex.Pattern


class AgeFieldState : TextFieldState(
    validator = ::isValid,
    errorMessage = ::errorMessage,
    maxChars = Input.MAX_AGE_CHARS
)

private const val VALIDATION_REGEX = "^[1-9]?\\d|[1-9]\$"

private fun isValid(age: String): Boolean {
    return age.isEmpty() || Pattern.matches(VALIDATION_REGEX, age)

}

private fun errorMessage(username: String) = "Age $username is invalid"