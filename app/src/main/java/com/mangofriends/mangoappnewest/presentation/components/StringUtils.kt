package com.mangofriends.mangoappnewest.presentation.components

class StringUtils {
    companion object {
        fun limitString(string: String, limit: Int): String {
            return if (string.length > limit) {
                string.take(limit) + "..."
            } else
                string
        }
    }
}