package com.example.GuideExpert.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.GuideExpert.domain.models.RemoteKey

@Entity("RemoteKey")
data class RemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextOffset: Int,
)

fun RemoteKeyEntity.toRemoteKey() = RemoteKey(
    id = id,
    nextOffset = nextOffset
)