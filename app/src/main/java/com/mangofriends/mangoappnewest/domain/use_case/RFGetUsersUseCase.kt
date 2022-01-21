package com.mangofriends.mangoappnewest.domain.use_case

import android.util.Log
import com.mangofriends.mangoappnewest.common.Resource
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RFGetUsersUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(uid: String): Flow<Resource<List<UserProfile>>> = flow {
        try {
            emit(Resource.Loading())
            val profiles = repository.getProfiles(uid)
            emit(Resource.Success(profiles))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))

        } catch (e: IOException) {
            Log.d("BLAB", e.message ?: "null")
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

}