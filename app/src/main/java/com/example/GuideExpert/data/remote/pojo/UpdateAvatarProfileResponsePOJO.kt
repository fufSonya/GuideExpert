package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName


data class UpdateAvatarProfileResponsePOJO(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("idAvatar")
    val idAvatar: Int?
)