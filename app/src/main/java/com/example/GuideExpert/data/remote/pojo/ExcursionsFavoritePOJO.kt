package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class ExcursionsFavoritePOJO (
    @SerializedName("excursions")
    val excursions: List<ExcursionPOJO>
)