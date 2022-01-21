package com.mangofriends.mangoappnewest.domain.model.firebase_models

data class FBRequestCall(
    var status: Int = 0,
    var message: String = "NO MESSAGE",
    var profiles: List<FBUserProfile> = emptyList()
)