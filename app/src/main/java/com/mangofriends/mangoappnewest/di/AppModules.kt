package com.mangofriends.mangoappnewest.di

import com.mangofriends.mangoappnewest.BuildConfig
import com.mangofriends.mangoappnewest.common.Constants
import com.mangofriends.mangoappnewest.data.api.MangoApi
import com.mangofriends.mangoappnewest.data.repository.FirebaseRepositoryImpl
import com.mangofriends.mangoappnewest.data.repository.InputRepositoryImpl
import com.mangofriends.mangoappnewest.data.repository.UserProfileRepositoryImpl
import com.mangofriends.mangoappnewest.domain.repository.FirebaseRepository
import com.mangofriends.mangoappnewest.domain.repository.InputRepository
import com.mangofriends.mangoappnewest.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHTTPClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.CONNECTION_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMangoApi(okHttpClient: OkHttpClient): MangoApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(api: MangoApi): UserProfileRepository {
        return UserProfileRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepositoryImpl()
    }

    @Provides
    fun provide(): InputRepository {
        return InputRepositoryImpl()
    }

}
