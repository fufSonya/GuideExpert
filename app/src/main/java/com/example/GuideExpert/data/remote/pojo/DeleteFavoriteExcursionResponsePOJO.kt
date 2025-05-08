package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class DeleteFavoriteExcursionResponsePOJO(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("excursion")
    val excursion: ExcursionFavoritePOJO,
)
