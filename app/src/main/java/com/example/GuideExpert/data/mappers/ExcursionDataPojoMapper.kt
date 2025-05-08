package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.ImagePreviewFavoriteEntity
import com.example.GuideExpert.data.local.models.ImagePreviewFilterEntity
import com.example.GuideExpert.data.local.models.ImagePreviewSearchEntity
import com.example.GuideExpert.data.remote.pojo.ExcursionDataPOJO
import com.example.GuideExpert.data.remote.pojo.ImagePOJO
import com.example.GuideExpert.domain.models.Banner
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.Image

fun ExcursionDataPOJO.toExcursionData() = ExcursionData(excursionId = excursionId,
    title = title, owner = owner,text = text, description = description,
    group = group, images = images.map{it.toImage()})

fun ImagePOJO.toImage() = Image(id,excursionId,url)

fun Image.toImagePOJO() = ImagePOJO(id,excursionId,url)

fun ImagePOJO.toImagePreviewSearchEntity() = ImagePreviewSearchEntity(
    id = id, excursionId = excursionId, url = url
)

fun ImagePOJO.toImagePreviewFilterEntity() = ImagePreviewFilterEntity(
    id = id, excursionId = excursionId, url = url
)

fun ImagePOJO.toImagePreviewFavoriteEntity() = ImagePreviewFavoriteEntity(
    id = id, excursionId = excursionId, url = url
)

fun ExcursionData.toExcursion() = Excursion(id = excursionId,title= title,description = description,
    images= images)

fun Image.toImagePreviewFavoriteEntity() = ImagePreviewFavoriteEntity(
    id = id, excursionId = excursionId, url = url
)