package com.example.GuideExpert.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class ExcursionsFavoriteWithData(
    @Embedded val excursion: ExcursionsFavoriteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "excursionId")
    val images: List<ImagePreviewFavoriteEntity>
)
