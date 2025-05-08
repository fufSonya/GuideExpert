package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.ExcursionFilterEntity
import com.example.GuideExpert.data.local.models.ExcursionFilterWithData
import com.example.GuideExpert.data.local.models.ExcursionSearchEntity
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.domain.models.Excursion

fun ExcursionFilterEntity.toExcursion() = Excursion(id,title, description,images,idSort)

fun ExcursionFilterWithData.toExcursionFilterEntity() = ExcursionFilterEntity(id = excursion.id,
    title = excursion.title, description = excursion.description, idSort = excursion.idSort, images = images.map { it.toImage() }
)


fun ExcursionFilterWithData.toExcursion() = Excursion(id=excursion.id,title = excursion.title,
    description = excursion.description, idSort = excursion.idSort,images = images.map{it.toImage()})

