package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.ExcursionSearchEntity
import com.example.GuideExpert.data.local.models.ExcursionFilterEntity
import com.example.GuideExpert.data.local.models.ExcursionFilterWithData
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.data.remote.pojo.ExcursionPOJO
import com.example.GuideExpert.domain.models.Excursion

fun ExcursionPOJO.toExcursion() = Excursion(id = id,title = title, description = description,images =images.map { it.toImage() },idSort = idSort ?: 0, timestamp = timestamp ?: 0)

fun Excursion.toExcursionPOJO() = ExcursionPOJO(id,title, description,images.map{ it.toImagePOJO()},idSort ?: 0, timestamp = timestamp ?: 0)

fun ExcursionPOJO.toExcursionSearchEntity() = ExcursionSearchEntity(id,title, description,idSort?: 0,images.map { it.toImage() })

fun ExcursionPOJO.toExcursionFilterEntity() = ExcursionFilterEntity(id,title, description,idSort?: 0,images.map { it.toImage() })


fun ExcursionPOJO.toExcursionSearchWithData() = ExcursionSearchWithData(
    ExcursionSearchEntity(id,title, description,idSort?: 0,images.map{it.toImage()}),
    images.map{ it.toImagePreviewSearchEntity() }
)

fun ExcursionPOJO.toExcursionFilterWithData() = ExcursionFilterWithData(
    ExcursionFilterEntity(id,title, description,idSort?: 0,images.map{it.toImage()}),
    images.map{ it.toImagePreviewFilterEntity() }
)