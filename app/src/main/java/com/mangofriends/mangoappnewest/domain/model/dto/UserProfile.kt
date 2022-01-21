package com.mangofriends.mangoappnewest.domain.model.dto

data class UserProfile(
    val age: Int = -1,
    val bio: String = "",
    val gender: String = "",
    val lastname: String = "",
    val location: String = "",
    val name: String = "",
    val occupation: String = "",
    val profile_image_urls: List<ProfileImageUrl> = emptyList(),
    val reg_date: Long = -1,
    val tags: List<Tag> = emptyList(),
    val uid: String = "",
    val university: String = "",
    val zodiac_sign: String = "",

    val search_min_age: Int = 0,
    val search_max_age: Int = 50,
    val search_city: String = "",
    val search_ignore_interest_tags: Boolean = false
)