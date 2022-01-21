package com.mangofriends.mangoappnewest.domain.repository

import com.mangofriends.mangoappnewest.presentation.input.*

interface InputRepository {
    fun getAgeFieldState(): AgeFieldState
    fun getEmailFieldState(): EmailFieldState
    fun getLongTextFieldState(): LongTextFieldState
    fun getPasswordFieldState(): PasswordFieldState
    fun getShortASCIIFieldState(): ShortASCIIFieldState
    fun getStandardNameFieldState(): StandardNameFieldState

}