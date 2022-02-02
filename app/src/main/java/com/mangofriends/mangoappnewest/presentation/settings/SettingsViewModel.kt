package com.mangofriends.mangoappnewest.presentation.settings

import androidx.lifecycle.ViewModel
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.use_case.FBCheckMatchUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBLikeUseCase
import com.mangofriends.mangoappnewest.domain.use_case.FBLogOutUseCase
import com.mangofriends.mangoappnewest.domain.use_case.RFGetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
//class SettingsViewModel @Inject constructor(
//    private val getUsersUseCase: RFGetUsersUseCase,
//    private val likeUseCase: FBLikeUseCase,
//    private val checkMatchUseCase: FBCheckMatchUseCase,
//    private val logOutUseCase: FBLogOutUseCase,
//    firebaseRepository: FirebaseRepository
//) : ViewModel() {