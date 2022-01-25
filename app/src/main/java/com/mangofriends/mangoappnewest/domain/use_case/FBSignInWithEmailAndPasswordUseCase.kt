package com.mangofriends.mangoappnewest.domain.use_case

import android.util.Log
import androidx.navigation.NavController
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FBSignInWithEmailAndPasswordUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(
        userCredentials: UserCredentials,
        navController: NavController,
        onError: (String) -> Unit
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val authResult =
                repository.signInWithEmailAndPassword(userCredentials, navController, onError)
            if (authResult.status == Constants.CODE_SUCCESS) {
                emit(Resource.Success(authResult.message))
            } else
                emit(Resource.Error(authResult.message))
        }
//        catch (e: FirebaseAuthWeakPasswordException) {
//            emit(Resource.Error(e.localizedMessage ?: "Weak Password"))
//        } catch (e: FirebaseAuthInvalidCredentialsException) {
//            emit(Resource.Error(e.localizedMessage ?: "Invalid Credentials"))
//        } catch (e: FirebaseAuthUserCollisionException) {
//            emit(Resource.Error(e.localizedMessage ?: "User collisions"))
//        }
        catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            Log.d("ERROR CATCH", e.message.toString())
            emit(Resource.Error(e.localizedMessage ?: "Generic exception"))
        }
    }

}
