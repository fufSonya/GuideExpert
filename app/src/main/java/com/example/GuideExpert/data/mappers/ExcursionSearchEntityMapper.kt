package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.ExcursionSearchEntity
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.domain.models.Excursion

fun ExcursionSearchEntity.toExcursion() = Excursion(id,title, description,images,idSort)

//fun Excursion.toExcursionSearchEntity() = ExcursionSearchEntity(id,title, description,photo)

//fun ExcursionSearchEntity.toExcursionPOJO() = ExcursionPOJO(id,title, description,photo)

fun ExcursionSearchWithData.toExcursionSearchEntity() = ExcursionSearchEntity(id = excursion.id,
    title = excursion.title, description = excursion.description, idSort = excursion.idSort, images = images.map { it.toImage() }
    )


fun ExcursionSearchWithData.toExcursion() = Excursion(id=excursion.id,title = excursion.title,
    description = excursion.description, images = images.map{it.toImage()}, idSort = excursion.idSort)