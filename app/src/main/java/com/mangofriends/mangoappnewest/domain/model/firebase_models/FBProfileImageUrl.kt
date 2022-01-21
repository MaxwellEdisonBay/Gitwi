package com.mangofriends.mangoappnewest.domain.model.firebase_models

import com.google.firebase.database.PropertyName

data class FBProfileImageUrl(
    @PropertyName("img_id") val img_id: String,
    @PropertyName("index") val index: Int,
    @PropertyName("url") val url: String
) {
    constructor() : this("", -1, "")
}