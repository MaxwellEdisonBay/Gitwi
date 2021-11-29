package com.mangofriends.mangoappnewest.data.repository

import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val fb : FirebaseRepository
) {
    fun registerWithEmailAndPassword(credentials: UserCredentials) {
        return fb.registerWithEmailAndPassword(credentials)
    }
}