package com.mangofriends.mangoappnewest.domain.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DTOProfileImageUrl(
    val img_id: String,
    val index: Int,
    val url: String
) : Parcelable