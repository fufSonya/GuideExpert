package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class BookingExcursionPOJO(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
)
