package com.mangofriends.mangoappnewest.domain.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DTOTag(
    val tag_id: String,
    val text: String,
    val tier: Int
) : Parcelable