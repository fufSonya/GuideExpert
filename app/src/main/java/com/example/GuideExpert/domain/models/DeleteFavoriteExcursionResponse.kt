package com.example.GuideExpert.domain.models

data class DeleteFavoriteExcursionResponse(
    val success: Boolean = false,
    val message: String = "",
    val excursion: ExcursionFavorite? = null,
)
