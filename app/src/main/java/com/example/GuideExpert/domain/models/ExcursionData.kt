package com.example.GuideExpert.domain.models

class ExcursionData(
    val excursionId: Int = 1,
    val title: String ="",
    val owner: Int = 1,
    val text: String = "",
    val description: String = "",
    val group: Int = 1,
    val images: List<Image> = listOf(),
)