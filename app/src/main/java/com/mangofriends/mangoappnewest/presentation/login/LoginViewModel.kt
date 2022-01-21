package com.mangofriends.mangoappnewest.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBSignInWithEmailAndPasswordUseCase
import com.mangofriends.mangoappnewest.presentation.input.EmailFieldState
import com.mangofriends.mangoappnewest.presentation.input.PasswordFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithCredUseCase: FBSignInWithEmailAndPasswordUseCase,
    val firebaseRepository: FirebaseRepository

) : ViewModel() {

    val emailState = EmailFieldState()
    val passwordState = PasswordFieldState()
    var isLogged: Boolean = FirebaseAuth.getInstance().currentUser != null

    init {
//        firebaseRepository.signOut()
        Log.d("LoginViewModel", "isLogged = $isLogged")
    }

    fun performLoginWithCredentials(
        credentials: UserCredentials,
        navController: NavController,
        onError: (String) -> Unit
    ) {
        loginWithCredUseCase(credentials, navController, onError).launchIn(viewModelScope)
    }

    fun isUserDataCorrect(): Boolean {
        return emailState.error == null
                && passwordState.error == null
                && emailState.text.isNotBlank()
                && passwordState.text.isNotBlank()
    }
}