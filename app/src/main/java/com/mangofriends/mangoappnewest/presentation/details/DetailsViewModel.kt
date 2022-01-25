package com.mangofriends.mangoappnewest.presentation.details

import androidx.lifecycle.ViewModel
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository
) : ViewModel() {

}