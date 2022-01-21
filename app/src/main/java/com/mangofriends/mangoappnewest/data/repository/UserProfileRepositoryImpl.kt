package com.mangofriends.mangoappnewest.data.repository

import com.mangofriends.mangoappnewest.data.api.MangoApi
import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile
import com.mangofriends.mangoappnewest.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val api: MangoApi
) : UserProfileRepository {

    override suspend fun getProfiles(uid: String): List<UserProfile> {
        return api.getProfiles(uid)
    }

}