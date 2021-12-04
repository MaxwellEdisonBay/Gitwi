package com.mangofriends.mangoappnewest.domain.use_case

import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.FBUserProfile
import com.mangofriends.mangoappnewest.domain.model.firebase_models.UserCredentials
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FBRegisterWithCredentialsUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(userCredentials: UserCredentials): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val authResult = repository.registerWithEmailAndPassword(userCredentials)
            if (authResult.status == Constants.CODE_SUCCESS)
                emit(Resource.Success(authResult.message))
            else
                emit(Resource.Error(authResult.message))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))

        } catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

}