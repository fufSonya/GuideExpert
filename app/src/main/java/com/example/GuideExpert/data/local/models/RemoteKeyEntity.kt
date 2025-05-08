package com.example.GuideExpert.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("RemoteKey")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextOffset: Int,
)
