package com.mangofriends.mangoappnewest.presentation.main

import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import com.mangofriends.mangoappnewest.presentation.main.cards.UserCard

data class ProfileListState(
    var isLoading: Boolean = true,
    val profiles: List<DTOUserProfile> = emptyList(),
    val error: String = ""
) {
    fun toUserCardList(): List<UserCard> {
        val list = mutableListOf<UserCard>()
        this.profiles.forEachIndexed { index, dtoUserProfile ->
            list.add(UserCard(index, dtoUserProfile))
        }
        return list.toList()
    }
}
