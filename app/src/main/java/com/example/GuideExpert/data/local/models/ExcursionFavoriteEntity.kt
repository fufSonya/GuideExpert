package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExcursionFavoriteEntity(
    @ColumnInfo val id: Int,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo val excursionId: Int,
    @ColumnInfo val timestamp: Int,
)
