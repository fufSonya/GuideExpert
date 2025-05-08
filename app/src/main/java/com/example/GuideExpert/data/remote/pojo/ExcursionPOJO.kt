package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class ExcursionPOJO (
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("images")
    val images: List<ImagePOJO>,

    @SerializedName("idSort")
    val idSort: Int?,

    @SerializedName("timestamp")
    val timestamp: Int?,
)