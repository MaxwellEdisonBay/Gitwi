package com.mangofriends.mangoappnewest.presentation.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBRegisterWithCredentialsUseCase
import com.mangofriends.mangoappnewest.domain.use_case.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val firebaseRepository: FirebaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ProfileListState())
    val state: State<ProfileListState> = _state

    init {

        Log.d("ViewModel","Created model")

        savedStateHandle.get<String>(Constants.PARAM_UID)?.let {
            getProfiles(it)
            Log.d("ViewModel","Param passed $it")
        }
    }

    private fun getProfiles(uid: String) {
        getUsersUseCase(uid).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ProfileListState(profiles = result.data ?: emptyList())
                    Log.d("ViewModel","Status Success")
                }
                is Resource.Error -> {
                    _state.value = ProfileListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                    Log.d("ViewModel","Status Error")
                    Log.d("ViewModel",result.message?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = ProfileListState(isLoading = true)
                    Log.d("ViewModel","Status Loading")
                }
            }
        }.launchIn(viewModelScope)
    }

}