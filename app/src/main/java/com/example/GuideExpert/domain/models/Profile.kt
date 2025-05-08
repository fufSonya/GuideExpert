package com.example.GuideExpert.domain.models

import java.util.Date

data class Profile(
    val id: Int,
    val login: String,
    val realName: String,
    val firstName: String,
    val lastName: String,
    val sex: String?,
    val email: String,
    val birthday: Date,
    val phone: String,
    val avatar: Avatar?
)
