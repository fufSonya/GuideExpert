package com.example.GuideExpert.domain.models

data class ExcursionFavoriteResponse(
    val success: Boolean = false,
    val message: String ="",
    val excursions:List<ExcursionFavorite> = listOf()
)

data class ExcursionFavorite(
    val id: Int,
    val excursionId: Int,
    val timestamp: Int
)