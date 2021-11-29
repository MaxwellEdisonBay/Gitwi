package com.mangofriends.mangoappnewest.di

import com.mangofriends.mangoappnewest.BuildConfig
import com.mangofriends.mangoappnewest.data.api.MangoApi
import com.mangofriends.mangoappnewest.data.repository.UserProfileRepositoryImpl
import com.mangofriends.mangoappnewest.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMangoApi() : MangoApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(api:MangoApi): UserProfileRepository{
        return UserProfileRepositoryImpl(api)
    }
}
