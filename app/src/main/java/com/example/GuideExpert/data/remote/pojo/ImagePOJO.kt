package com.example.GuideExpert.data.remote.pojo

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ImagePOJO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("excursionId")
    val excursionId: Int,

    @SerializedName("url")
    val url: String,
)