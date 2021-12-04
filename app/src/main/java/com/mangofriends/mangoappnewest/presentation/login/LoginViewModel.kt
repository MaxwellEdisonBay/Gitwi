package com.mangofriends.mangoappnewest.presentation.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ivanteslenko.ui.input.EmailFieldState
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBRegisterWithCredentialsUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBSignInWithEmailAndPasswordUseCase
import com.mangofriends.mangoappnewest.presentation.components.Screen
import com.mangofriends.mangoappnewest.presentation.ui.input.PasswordFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val regWithCredUseCase: FBRegisterWithCredentialsUseCase,
    private val loginWithCredUseCase : FBSignInWithEmailAndPasswordUseCase,
    val firebaseRepository: FirebaseRepository

) : ViewModel() {

    val emailState = EmailFieldState()
    val passwordState = PasswordFieldState()

    init {
        firebaseRepository.signOut()
        Log.d("LoginViewModel", firebaseRepository.checkIfLoggedIn().toString())

    }

    fun performRegisterWithCredentials(credentials: UserCredentials) {
        regWithCredUseCase(credentials).launchIn(viewModelScope)
    }

    fun performLoginWithCredentials (credentials: UserCredentials, navController: NavController){
        loginWithCredUseCase(credentials, navController).launchIn(viewModelScope)
    }

    fun isUserDataCorrect(): Boolean {
        return emailState.error == null
                && passwordState.error == null
                && emailState.text.isNotBlank()
                && passwordState.text.isNotBlank()
    }
}