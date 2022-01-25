package com.mangofriends.mangoappnewest.domain.repository

import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile

interface UserProfileRepository {
    suspend fun getProfiles(): List<DTOUserProfile>
}