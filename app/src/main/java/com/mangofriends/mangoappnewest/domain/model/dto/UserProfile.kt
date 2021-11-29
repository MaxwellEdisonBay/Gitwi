package com.mangofriends.mangoappnewest.domain.model.dto

import com.mangofriends.mangoappnewest.domain.model.dto.ProfileImageUrl
import com.mangofriends.mangoappnewest.domain.model.dto.Tag

data class UserProfile(
    val age: Int,
    val bio: String,
    val gender: String,
    val lastname: String,
    val location: String,
    val name: String,
    val occupation: String,
    val profile_image_urls: List<ProfileImageUrl>,
    val reg_date: Int,
    val tags: List<Tag>,
    val uid: String,
    val university: String,
    val zodiac_sign: String
)