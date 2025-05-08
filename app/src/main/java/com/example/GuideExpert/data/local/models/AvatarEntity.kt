package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["profileId"])],
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("profileId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class AvatarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val profileId: Int,
    @ColumnInfo val url: String
)