package com.mangofriends.mangoappnewest.presentation.ui.input

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mangofriends.mangoappnewest.common.Constants.MAX_TEXTFIELD_CHARS


open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorMessage: (String) -> String
) {
    var text by mutableStateOf("")
    var error by mutableStateOf<String?>(null)

    fun validate() {
        error = if (validator(text)) {
            null
        } else {
            errorMessage(text)
        }
    }

    fun onChanged(text: String) {
        if (text.length <= MAX_TEXTFIELD_CHARS) {
            var trimmed = text.replace(" ", "")
            trimmed = trimmed.replace("\n", "")
            this.text = trimmed
        }
    }
}