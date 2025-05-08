package com.example.GuideExpert.data.remote.services

import com.example.GuideExpert.data.remote.pojo.DeleteFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionsFavoritePOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionsFavoriteResponsePOJO
import com.example.GuideExpert.data.remote.pojo.ProfilePOJO
import com.example.GuideExpert.data.remote.pojo.RemoveAvatarProfileResponsePOJO
import com.example.GuideExpert.data.remote.pojo.RestoreFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.SetFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.UpdateAvatarProfileResponsePOJO
import com.example.GuideExpert.data.remote.pojo.UpdateProfileResponsePOJO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.util.Date

interface ProfileService {

    @GET("getprofile.php")
    suspend fun getProfile(@Query("id") profileId:Int): Response<ProfilePOJO>

    @Multipart
    @POST("updateavatar.php")
    suspend fun updateAvatarProfile(
        @Part("profile_id") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<UpdateAvatarProfileResponsePOJO>

    @GET("removeavatar.php")
    suspend fun removeAvatarProfile(@Query("profile_id") profileId:Int): Response<RemoveAvatarProfileResponsePOJO>

    @GET("updateprofile.php")
    suspend fun updateProfile(@Query("profile_id") profileId:Int, @Query("first_name") firstName:String,
                              @Query("last_name") lastName:String, @Query("sex") sex:String?,
                              @Query("email") email:String, @Query("birthday") birthday: Date
                              ): Response<UpdateProfileResponsePOJO>

    @GET("getlistidfavoriteexcursion.php")
    suspend fun getExcursionsFavoriteId(@Query("profile_id") profileId:Int): Response<ExcursionsFavoriteResponsePOJO>

    @GET("setfavoriteexcursion.php")
    suspend fun setExcursionFavorite(@Query("profile_id") profileId:Int,@Query("excursion_id") excursionId:Int): Response<SetFavoriteExcursionResponsePOJO>

    @GET("removefavoriteexcursion.php")
    suspend fun removeExcursionFavorite(@Query("profile_id") profileId:Int,@Query("excursion_id") excursionId:Int): Response<DeleteFavoriteExcursionResponsePOJO>

    @GET("excursionsfavorite.php")
    suspend fun getExcursionsFavorite(@Query("profile_id") profileId:Int): Response<ExcursionsFavoritePOJO>

    @GET("restorefavoriteexcursion.php")
    suspend fun restoreExcursionFavorite(@Query("profile_id") profileId:Int,@Query("excursion_id") excursionId:Int,@Query("timestamp") timestamp:Int): Response<RestoreFavoriteExcursionResponsePOJO>
}