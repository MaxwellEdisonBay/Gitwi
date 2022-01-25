package com.mangofriends.mangoappnewest.data.repository

import com.mangofriends.mangoappnewest.domain.repository.InputRepository
import com.mangofriends.mangoappnewest.presentation.input.*

class InputRepositoryImpl : InputRepository {
    override fun getAgeFieldState(): AgeFieldState {
        return AgeFieldState()
    }

    override fun getEmailFieldState(): EmailFieldState {
        return EmailFieldState()
    }

    override fun getLongTextFieldState(): LongTextFieldState {
        return LongTextFieldState()
    }

    override fun getPasswordFieldState(): PasswordFieldState {
        return PasswordFieldState()
    }

    override fun getShortASCIIFieldState(): ShortASCIIFieldState {
        return ShortASCIIFieldState()
    }

    override fun getStandardNameFieldState(): StandardNameFieldState {
        return StandardNameFieldState()
    }
}