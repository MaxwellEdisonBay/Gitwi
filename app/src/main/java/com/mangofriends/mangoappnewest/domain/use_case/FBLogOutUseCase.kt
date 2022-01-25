package com.mangofriends.mangoappnewest.domain.use_case

import android.util.Log
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import javax.inject.Inject

class FBLogOutUseCase @Inject constructor(

    private val repository: FirebaseRepository
) {
    operator fun invoke(navController: NavController) {
        repository.signOut(navController)
        Log.d("AUTH", "Signed out")
    }

}