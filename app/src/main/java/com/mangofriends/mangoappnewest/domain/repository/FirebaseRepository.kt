package com.mangofriends.mangoappnewest.domain.repository

import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBRequestCall
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.presentation.main.MainViewModel
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel

interface FirebaseRepository {
    suspend fun registerWithEmailAndPassword(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit = {}
    ): FBRequestCall

    suspend fun signInWithEmailAndPassword(
        credentials: UserCredentials,
        navController: NavController,
        onError: (String) -> Unit = {}
    ): FBRequestCall

    fun uploadImagesToFireCloud(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ): FBRequestCall

    fun uploadRegistrationDataToFirebase(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ): FBRequestCall

    fun checkIfLoggedIn(): Boolean

    fun getUid(): String

    fun likeUser(myUid: String, uid: String, onLike: () -> Unit = {}): FBRequestCall

    fun checkMatch(
        currentUser: FBUserProfile,
        user: DTOUserProfile,
        onMatch: (Boolean) -> Unit = {}
    ): FBRequestCall

    fun signOut(navController: NavController)

    fun loadMatches(viewModel: MainViewModel)

    fun getCurrentUser(viewModel: MainViewModel, onFirstLoad: () -> Unit)
}