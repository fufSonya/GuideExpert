package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class ExcursionsFavoriteResponsePOJO(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("excursions")
    val excursions: List<ExcursionFavoritePOJO>,
)
