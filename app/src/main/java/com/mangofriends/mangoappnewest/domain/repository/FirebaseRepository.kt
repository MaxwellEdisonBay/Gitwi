package com.mangofriends.mangoappnewest.domain.repository

import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials

interface FirebaseRepository {
    fun registerWithEmailAndPassword(credentials: UserCredentials)

    fun uploadImagesToFireCloud(uris: List<String>) : List<String>

    fun uploadregistrationDataToFirebase(profile : UserProfile)

}