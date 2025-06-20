package com.example.GuideExpert.data.remote.services

import com.example.GuideExpert.data.remote.pojo.ExcursionDataPOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionsPagingPOJO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ExcursionService {
    @GET("excursionsearch.php")
    suspend fun getExcursionsSearchPaging(@Query("offset") offset:Int, @Query("limit") limit:Int,
                                          @Query("query") query:String): Response<ExcursionsPagingPOJO>

    @FormUrlEncoded
    @POST("excursionsfilter.php")
    suspend fun getExcursionsFiltersPaging(@Query("offset") offset:Int, @Query("limit") limit:Int,
                                           @Query("sort") sort:Int,@Field("categories[]") categories: List<Int>,
                                           @Field("duration[]") duration: List<Int>,@Field("group[]") group: List<Int>): Response<ExcursionsPagingPOJO>


    @GET("excursiondetail.php")
    suspend fun getExcursionData(@Query("id") id:Int): Response<ExcursionDataPOJO>
}