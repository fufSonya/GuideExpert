package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class BannerPOJO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("photo")
    val photo: String,
)
