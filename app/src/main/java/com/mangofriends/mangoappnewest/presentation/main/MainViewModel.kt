package com.mangofriends.mangoappnewest.presentation.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBUserProfile
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBCheckMatchUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBLikeUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBLogOutUseCase
import com.mangofriends.mangoappnewest.domain.use_case.RFGetUsersUseCase
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

    private val _state = mutableStateOf(ProfileListState())
    val state: State<ProfileListState> = _state
    private var backButtonTime = Constants.START_TIME
    val currentUser = mutableStateOf(FBUserProfile())
    val isFirstLoad = mutableStateOf(true)

//    private var cardData = mutableStateListOf<UserCard>()

    val topCardIndex = mutableStateOf(0)


    init {
        Log.d("MainViewModel", "Created model")
        Log.d("MainViewModel", "isFirst Load = ${isFirstLoad.value}")

        firebaseRepository.getCurrentUser(this, onFirstLoad = {
            getProfiles()
        })
    }


    fun likeUser(myUid: String, uid: String, onLike: () -> Unit = {}) {
        likeUseCase(myUid, uid, onLike).launchIn(viewModelScope)
    }

    fun checkMatch(
        currentUser: FBUserProfile,
        user: DTOUserProfile,
        onMatch: (Boolean) -> Unit = {}
    ) {
        checkMatchUseCase(currentUser, user, onMatch).launchIn(viewModelScope)
    }

    fun logOut(navController: NavController) {
        logOutUseCase(navController)
    }

    fun getProfiles() {
        getUsersUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ProfileListState(profiles = result.data ?: emptyList())
//                    data.clear()
//                    for (i in 0 until _state.value.profiles.count()) {
//                        data.add(UserCard(i, state.value.profiles[i]))
//                    }
//                    var debugString = ""
////                    data.forEach { debugString += it.user.profile_image_urls[0].url + "\n" }
//                    Log.d("ARRR", debugString)
                    Log.d("GET PROFILES", "Status Success")
                    topCardIndex.value = 0
                    state.value.isLoading = false
                }
                is Resource.Error -> {
                    _state.value = ProfileListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                    state.value.isLoading = false

                    Log.d("GET PROFILES", "Status Error")
                    Log.d("GET PROFILES", result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = ProfileListState(isLoading = true)
                    Log.d("GET PROFILES", "Status Loading")
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