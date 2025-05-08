package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.GuideExpert.domain.models.Image

@Entity
data class ExcursionsFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val timestamp: Int,
    @Ignore var images: List<Image>
){
    constructor(id: Int, title: String, description: String, timestamp: Int) : this(id, title, description, timestamp, listOf())
}
