package com.mangofriends.mangoappnewest.domain.repository

import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile

interface UserProfileRepository {
    suspend fun getProfiles(uid: String): List<UserProfile>
}