package com.mangofriends.mangoappnewest.domain.model.firebase_models

import com.google.firebase.database.PropertyName


data class FBUserProfile(
    @PropertyName("age") val age: Int,
    @PropertyName("bio") val bio: String,
    @PropertyName("gender") val gender: String,
    @PropertyName("lastname") val lastname: String,
    @PropertyName("location") val location: String,
    @PropertyName("name") val name: String,
    @PropertyName("occupation") val occupation: String,
    @PropertyName("profile_image_urls") val profile_image_urls: List<FBProfileImageUrl>,
    @PropertyName("reg_date") val reg_date: Int,
    @PropertyName("tags") val tags: List<FBTag>,
    @PropertyName("uid") val uid: String,
    @PropertyName("university") val university: String,
    @PropertyName("zodiac_sign") val zodiac_sign: String
) {
    constructor() : this(
        0, "", "", "", "",
        "", "", emptyList(), 0, emptyList(),
        "", "", ""
    )
}