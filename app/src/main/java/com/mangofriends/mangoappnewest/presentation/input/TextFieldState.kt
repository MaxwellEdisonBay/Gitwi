package com.mangofriends.mangoappnewest.presentation.input

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorMessage: (String) -> String,
    private val maxChars: Int
) {
    var text by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
    val pattern = "\\s+".toRegex()

    fun validate() {
        error = if (validator(text)) {
            null
        } else {
            errorMessage(text)
        }
    }

    fun onChanged(text: String) {
        if (text.length <= maxChars) {
            var trimmed = text.replace(pattern, " ")
            trimmed = trimmed.trimStart()

//            var trimmed = text.replace(" ", "")
//            trimmed = trimmed.replace("\n", "")
            this.text = trimmed
        }
    }
}