package com.example.GuideExpert.domain.models

data class RestoreFavoriteExcursionResponse(
    val success: Boolean = false,
    val message: String = "",
    val excursion: ExcursionFavorite? = null,
)
