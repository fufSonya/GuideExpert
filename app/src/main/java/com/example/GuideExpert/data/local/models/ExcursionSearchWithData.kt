package com.example.GuideExpert.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class ExcursionSearchWithData(
    @Embedded val excursion: ExcursionSearchEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "excursionId")
    val images: List<ImagePreviewSearchEntity>
)
