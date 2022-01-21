package com.mangofriends.mangoappnewest.domain.model.firebase_models

data class FBLike(val uid: String = "", val timestamp: Long) {
    constructor() : this("", -1)
}
