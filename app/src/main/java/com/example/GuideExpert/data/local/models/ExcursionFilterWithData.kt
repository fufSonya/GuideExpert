package com.example.GuideExpert.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class ExcursionFilterWithData(
    @Embedded val excursion: ExcursionFilterEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "excursionId")
    val images: List<ImagePreviewFilterEntity>
)