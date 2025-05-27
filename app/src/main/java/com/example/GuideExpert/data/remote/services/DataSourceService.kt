package com.example.GuideExpert.data.remote.services

import com.example.GuideExpert.data.remote.pojo.ConfigPOJO
import com.example.GuideExpert.data.remote.pojo.ProfileYandexPOJO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DataSourceService {
    @GET("config.json")
    suspend fun getConfig(): Response<ConfigPOJO>

    @GET("loginyandex.php")
    suspend fun loginYandex(@Query("oauth_token") oauthToken:String): Response<ProfileYandexPOJO>

}