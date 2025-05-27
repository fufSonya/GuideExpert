package com.example.GuideExpert.data.remote.services

import com.example.GuideExpert.data.remote.pojo.BookingExcursionPOJO
import com.example.GuideExpert.data.remote.pojo.DeleteFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionsFavoritePOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionsFavoriteResponsePOJO
import com.example.GuideExpert.data.remote.pojo.RestoreFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.SetFavoriteExcursionResponsePOJO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExcursionAuthService {
    @GET("getlistidfavoriteexcursion.php")
    suspend fun getExcursionsFavoriteId(@Query("profile_id") profileId:Int): Response<ExcursionsFavoriteResponsePOJO>

    @GET("setfavoriteexcursion.php")
    suspend fun setExcursionFavorite(@Query("profile_id") profileId:Int, @Query("excursion_id") excursionId:Int): Response<SetFavoriteExcursionResponsePOJO>

    @GET("removefavoriteexcursion.php")
    suspend fun removeExcursionFavorite(@Query("profile_id") profileId:Int, @Query("excursion_id") excursionId:Int): Response<DeleteFavoriteExcursionResponsePOJO>

    @GET("excursionsfavorite.php")
    suspend fun getExcursionsFavorite(@Query("profile_id") profileId:Int): Response<ExcursionsFavoritePOJO>

    @GET("restorefavoriteexcursion.php")
    suspend fun restoreExcursionFavorite(@Query("profile_id") profileId:Int, @Query("excursion_id") excursionId:Int, @Query("timestamp") timestamp:Int): Response<RestoreFavoriteExcursionResponsePOJO>

    @GET("bookingexcursion.php")
    suspend fun bookingExcursion(@Query("profile_id") profileId:Int, @Query("excursion_id") excursionId:Int,
                                 @Query("count") count:String, @Query("email") email:String, @Query("phone") phone:String,
                                 @Query("comments") comments:String, @Query("date") date:String, @Query("time") time:String): Response<BookingExcursionPOJO>

}