package com.mangofriends.mangoappnewest.presentation.main

import com.mangofriends.mangoappnewest.domain.model.dto.UserProfile

data class ProfileListState(
    val isLoading: Boolean = false,
    val profiles: List<UserProfile> = emptyList(),
    val error: String = ""
)
