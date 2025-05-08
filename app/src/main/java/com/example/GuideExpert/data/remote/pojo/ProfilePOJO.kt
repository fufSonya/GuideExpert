package com.example.GuideExpert.data.remote.pojo

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProfilePOJO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("realName")
    val realName: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("sex")
    val sex: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("birthday")
    val birthday: Date,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("avatar")
    val avatar: AvatarPOJO?
)
