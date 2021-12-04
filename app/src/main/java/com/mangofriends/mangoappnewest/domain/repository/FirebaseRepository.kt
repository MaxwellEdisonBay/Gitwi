package com.mangofriends.mangoappnewest.domain.repository

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBRequestCall
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import kotlinx.coroutines.flow.FlowCollector

interface FirebaseRepository {
    suspend fun registerWithEmailAndPassword(credentials: UserCredentials) : FBRequestCall

    suspend fun signInWithEmailAndPassword(credentials: UserCredentials, navController: NavController): FBRequestCall

    fun uploadImagesToFireCloud(uris: List<String>) : List<String>

    fun uploadregistrationDataToFirebase(profile : UserProfile): MutableLiveData<FBRequestCall>

    fun checkIfLoggedIn():Boolean

    fun signOut()

    fun getUID() : String?
}