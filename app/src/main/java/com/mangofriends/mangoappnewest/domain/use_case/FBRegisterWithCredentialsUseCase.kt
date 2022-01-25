package com.mangofriends.mangoappnewest.domain.use_case

import android.util.Log
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.presentation.register.RegisterViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FBRegisterWithCredentialsUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(
        viewModel: RegisterViewModel,
        navController: NavController,
        onError: (String) -> Unit
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            viewModel.state.isLoading.value = true
            val authResult =
                repository.registerWithEmailAndPassword(viewModel, navController, onError)
            if (authResult.status == Constants.CODE_SUCCESS) {

                emit(Resource.Success(authResult.message))
            } else
                emit(Resource.Error(authResult.message))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            Log.d("ERROR CATCH", e.message.toString())
            emit(Resource.Error(e.message ?: "Generic exception"))
        }
    }

}