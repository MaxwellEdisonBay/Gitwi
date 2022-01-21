package com.mangofriends.mangoappnewest.domain.use_case

import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FBCheckMatchUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(myUid: String, uid: String, onMatch: () -> Unit): Flow<Resource<String>> =
        flow {
            try {
                emit(Resource.Loading())
                val authResult = repository.checkMatch(myUid, uid, onMatch)
                if (authResult.status == Constants.CODE_SUCCESS)
                    emit(Resource.Success(authResult.message))
                else
                    emit(Resource.Error(authResult.message))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))

            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            }
        }

}