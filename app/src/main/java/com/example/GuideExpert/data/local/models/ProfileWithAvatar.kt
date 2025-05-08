package com.example.GuideExpert.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithAvatar(
    @Embedded val profile: ProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "profileId")
    val avatar: AvatarEntity?
)