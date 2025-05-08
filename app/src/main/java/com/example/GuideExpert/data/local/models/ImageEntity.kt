package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["excursionId"])],
    foreignKeys = [
    ForeignKey(
        entity = ExcursionDataEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("excursionId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val excursionId: Int,
    @ColumnInfo val url: String
)