package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.ExcursionDataEntity
import com.example.GuideExpert.data.local.models.ImageEntity
import com.example.GuideExpert.data.local.models.ImagePreviewFavoriteEntity
import com.example.GuideExpert.data.local.models.ImagePreviewFilterEntity
import com.example.GuideExpert.data.local.models.ImagePreviewSearchEntity
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.Image

fun ExcursionData.toExcursionDataEntity() = ExcursionDataEntity(
    id = excursionId,title = title, description = description,text = text,owner=owner,group=group
)

fun Image.toImageEntity() = ImageEntity(
    id = id, excursionId = excursionId, url = url
)

fun ExcursionDataEntity.toExcursionData() = ExcursionData(
    excursionId = id,title = title, description = description,text = text,owner=owner,group=group
)

fun ImageEntity.toImage() = Image(
    id = id, excursionId = excursionId, url = url
)


fun ImagePreviewSearchEntity.toImage() = Image(
    id = id, excursionId = excursionId, url = url
)

fun ImagePreviewFilterEntity.toImage() = Image(
    id = id, excursionId = excursionId, url = url
)

fun ImagePreviewFavoriteEntity.toImage() = Image(
    id = id, excursionId = excursionId, url = url
)
