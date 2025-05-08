package com.example.GuideExpert.data.di

import com.example.GuideExpert.data.SessionManager
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.data.remote.services.ProfileService
import com.example.GuideExpert.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @NoAuth
    fun provideOkHttpClientNoAuth(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)

        okHttpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("User-Agent","GuideExpert")
                .build()
            chain.proceed(newRequest)
        }
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    @NoAuth
    fun provideRetrofitNoAuth(@NoAuth okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExcursionService(@NoAuth retrofit: Retrofit): ExcursionService {
        return retrofit.create(ExcursionService::class.java)
    }

    //------------
    //------------
    //------------

    @Provides
    @Auth
    fun provideOkHttpClientAuth(loggingInterceptor: HttpLoggingInterceptor, sessionManager: SessionManager): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)

        okHttpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
            val token = runBlocking {
                sessionManager.getAuthToken().first()
            }
            newRequest.addHeader("Authorization", "Bearer $token")
            newRequest.addHeader("User-Agent","GuideExpert")
            chain.proceed(newRequest.build())
        }
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    @Auth
    fun provideRetrofitAuth(@Auth okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

     @Provides
     @Singleton
     fun provideUserService(@Auth retrofit: Retrofit): ProfileService {
         return retrofit.create(ProfileService::class.java)
     }
}
