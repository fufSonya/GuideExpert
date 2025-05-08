package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class ConfigPOJO(
    @SerializedName("banners")
    val banners: List<BannerPOJO>,
)
