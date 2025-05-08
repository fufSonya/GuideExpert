package com.example.GuideExpert.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.GuideExpert.domain.models.Image

@Entity
data class ExcursionSearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val idSort: Int,
    @Ignore var images: List<Image>
){
    constructor(id: Int, title: String, description: String,idSort:Int) : this(id, title, description,idSort, listOf())
}