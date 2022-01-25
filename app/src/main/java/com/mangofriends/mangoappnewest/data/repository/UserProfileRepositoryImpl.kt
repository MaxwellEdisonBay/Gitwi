package com.mangofriends.mangoappnewest.data.repository

import com.mangofriends.mangoappnewest.data.api.MangoApi
import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val api: MangoApi,
    private val firebaseRepository: FirebaseRepository
) : UserProfileRepository {

    override suspend fun getProfiles(): List<DTOUserProfile> {
        return api.getProfiles(firebaseRepository.getUid())
    }

}