package com.mangofriends.mangoappnewest.domain.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DTOUserProfile(
    val age: Int = -1,
    val bio: String = "",
    val gender: String = "",
    val lastname: String = "",
    val location: String = "",
    val name: String = "",
    val occupation: String = "",

    val profile_image_urls: List<DTOProfileImageUrl> = emptyList(),
    val reg_date: Long = -1,
    val tags: List<DTOTag> = emptyList(),
    val uid: String = "",
    val university: String = "",
    val zodiac_sign: String = "",

    val search_min_age: Int = 0,
    val search_max_age: Int = 50,
    val search_city: String = "",
    val search_ignore_interest_tags: Boolean = false,
//    val liked : List<FBLike> = emptyList(),
//    val liked_me : List<FBLike> = emptyList(),
//    val matches : List<FBMatch> = emptyList(),
) : Parcelable