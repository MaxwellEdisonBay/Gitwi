package com.mangofriends.mangoappnewest.presentation.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBLike
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBCheckMatchUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBLikeUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBLogOutUseCase
import com.mangofriends.mangoappnewest.domain.use_case.RFGetUsersUseCase
import com.mangofriends.mangoappnewest.presentation.main.cards.UserCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsersUseCase: RFGetUsersUseCase,
    private val likeUseCase: FBLikeUseCase,
    private val checkMatchUseCase: FBCheckMatchUseCase,
    private val logOutUseCase: FBLogOutUseCase,
    firebaseRepository: FirebaseRepository
) : ViewModel() {
    var uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _state = mutableStateOf(ProfileListState())
    val state: State<ProfileListState> = _state
    private var backButtonTime = Constants.START_TIME;

    private var cardData = mutableStateListOf<UserCard>()

    val data = mutableStateOf(cardData)
    val topCardIndex = mutableStateOf(0)
    val drawerValues = mutableStateListOf<FBLike>()


    init {
        Log.d("ViewModel", "Created model")
        Log.d("FB Auth", "My UID $uid")
        firebaseRepository.loadMatches(this)
        getProfiles("uid")


    }


    fun likeUser(myUid: String, uid: String, onLike: () -> Unit = {}) {
        likeUseCase(myUid, uid, onLike).launchIn(viewModelScope)
    }

    fun checkMatch(myUid: String, uid: String, onMatch: () -> Unit = {}) {
        checkMatchUseCase(myUid, uid, onMatch).launchIn(viewModelScope)
    }

    fun logOut(navController: NavController) {
        logOutUseCase(navController)
    }

    fun getProfiles(uid: String) {
        getUsersUseCase(uid).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ProfileListState(profiles = result.data ?: emptyList())
                    cardData.clear()
                    for (i in 0 until _state.value.profiles.count()) {
                        cardData.add(UserCard(i, state.value.profiles[i]))
                    }
                    var stra = ""
                    cardData.forEach { stra += it.user.profile_image_urls[0].url + "\n" }
                    Log.d("ARRR", stra)
                    Log.d("ViewModel", "Status Success")
                }
                is Resource.Error -> {
                    _state.value = ProfileListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                    Log.d("ViewModel", "Status Error")
                    Log.d("ViewModel", result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = ProfileListState(isLoading = true)
                    Log.d("ViewModel", "Status Loading")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun isBackExit(): Boolean {
        return if (System.currentTimeMillis() - backButtonTime < Constants.BACK_PRESS_DELAY)
            true
        else {
            backButtonTime = System.currentTimeMillis()
            false
        }
    }

}