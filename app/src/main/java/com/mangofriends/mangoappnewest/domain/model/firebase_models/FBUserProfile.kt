package com.mangofriends.mangoappnewest.domain.model.firebase_models

import com.google.firebase.database.PropertyName


data class FBUserProfile(
    @PropertyName("age") val age: Int,
    @PropertyName("bio") val bio: String,
    @PropertyName("gender") val gender: String,
    @PropertyName("lastname") val lastname: String,
    @PropertyName("location") val location: String,
    @PropertyName("liked") val liked: Map<String, FBLike>,
    @PropertyName("liked_me") val liked_me: Map<String, FBLike>,
    @PropertyName("matches") val matches: Map<String, FBMatch>,
    @PropertyName("name") val name: String,
    @PropertyName("occupation") val occupation: String,
    @PropertyName("profile_image_urls") val profile_image_urls: Map<String, FBProfileImageUrl>,
    @PropertyName("reg_date") val reg_date: Long,
    @PropertyName("tags") val tags: Map<String, FBTag>,
    @PropertyName("uid") val uid: String,
    @PropertyName("university") val university: String,
    @PropertyName("zodiac_sign") val zodiac_sign: String,
    @PropertyName("search_ignore_interest_tags") val search_ignore_interest_tags: Boolean,
    @PropertyName("search_city") val search_city: String,

    @PropertyName("search_max_age") val search_max_age: Int,
    @PropertyName("search_min_age") val search_min_age: Int,

    ) {
    constructor() : this(
        0, "", "", "", "", emptyMap(), emptyMap(), emptyMap(),
        "", "", emptyMap(), 0, emptyMap(),
        "", "", "", false, "", 0, 50
    )
}