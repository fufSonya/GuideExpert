package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExcursionDataEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val text: String,
    @ColumnInfo val owner: Int,
    @ColumnInfo val group: Int,
)