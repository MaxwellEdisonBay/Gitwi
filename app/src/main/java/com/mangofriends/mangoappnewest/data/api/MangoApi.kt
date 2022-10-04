package com.mangofriends.mangoappnewest.data.api

import com.mangofriends.mangoappnewest.domain.model.dto.DTOUserProfile
import retrofit2.http.GET
import retrofit2.http.Path

interface MangoApi {
    @GET("get-users/{uid}")
    suspend fun getProfiles(@Path("uid") uid: String): List<DTOUserProfile>
}