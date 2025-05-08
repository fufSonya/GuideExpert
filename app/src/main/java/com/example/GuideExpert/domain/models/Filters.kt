package com.example.GuideExpert.domain.models

data class Filters(
    val sort: Int,
    val categories: List<Int> = listOf(),
    val duration: List<Int> = listOf(),
    val group: List<Int> = listOf()
)
