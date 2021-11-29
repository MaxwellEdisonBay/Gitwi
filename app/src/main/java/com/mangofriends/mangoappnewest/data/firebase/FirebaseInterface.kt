package com.mangofriends.mangoappnewest.data.firebase

import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials

interface FirebaseInterface {
    fun registerWithEmailAndPassword(credentials: UserCredentials)

    fun uploadImagesToFireCloud(uris: List<String>) : List<String>

    fun uploadregistrationDataToFirebase(profile : UserProfile)
}