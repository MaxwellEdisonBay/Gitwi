package com.mangofriends.mangoappnewest.domain.model.firebase_models

import com.google.firebase.database.PropertyName

data class FBTag(
    @PropertyName("tag_id") val tag_id: String,
    @PropertyName("text") val text: String,
    @PropertyName("tier")val tier: Int
){
    constructor() : this("","",-1)
}