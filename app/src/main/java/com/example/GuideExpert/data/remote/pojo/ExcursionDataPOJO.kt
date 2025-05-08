package com.example.GuideExpert.data.remote.pojo

import com.example.GuideExpert.R
import com.google.gson.annotations.SerializedName

data class ExcursionDataPOJO(
    @SerializedName("id")
    val excursionId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("owner")
    val owner: Int,

    @SerializedName("group")
    val group: Int,

    @SerializedName("images")
    val images: List<ImagePOJO>,
    )