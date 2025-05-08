package com.example.GuideExpert.data

import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.FilterType

object DataProvider {

    val filtersBar = listOf(
        Filter(id = 1, name = "Топ",type = FilterType.Sort, description = ""),
        Filter(id = 2, name = "4-6 часов",type = FilterType.Duration, description = ""),
        Filter(id = 1, name = "Обзорные",type = FilterType.Categories, description = ""),
        Filter(id = 1, name = "Групповые",type = FilterType.Groups, description = ""),
    )

    val filtersSort = listOf(
        Filter(id = 1, name = "Топ",type = FilterType.Sort, description = ""),
        Filter(id = 2, name = "Новые",type = FilterType.Sort, description = ""),
        Filter(id = 3, name = "По умолчанию",type = FilterType.Sort, description = ""),)

    val filtersCategories = listOf(
        Filter(id = 1, name = "Обзорные",type = FilterType.Categories, description = "Обзорная"),
        Filter(id = 2, name = "Детские",type = FilterType.Categories, description = "Детская"),
        Filter(id = 3, name = "Водные",type = FilterType.Categories, description = "Водная")
    )

    val filtersDuration = listOf(
        Filter(id = 1, name = "Короткие (1-3 часа)",type = FilterType.Duration, description = "Короткая"),
        Filter(id = 2, name = "Долгие (4-6 часов)",type = FilterType.Duration, description = "Длинная"),
        Filter(id = 3, name = "Весь день",type = FilterType.Duration, description = "Весь день")
    )

    val filtersGroups = listOf(
        Filter(id = 1, name = "Групповые",type = FilterType.Groups, description = "Групповая"),
        Filter(id = 2, name = "Индивидуальные",type = FilterType.Groups, description = "Индивидуальная"),
    )

    var sortDefault = filtersSort.get(2).id
}