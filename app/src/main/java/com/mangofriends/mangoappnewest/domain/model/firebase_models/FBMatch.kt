package com.mangofriends.mangoappnewest.domain.model.firebase_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FBMatch(
    val uid: String,
    val timestamp: Long,
    val photo: String,
    val name: String,
    val last_message: String,
    val last_message_time: Long
) : Parcelable {
    constructor() : this("", -1, "", "", "", -1)
}
