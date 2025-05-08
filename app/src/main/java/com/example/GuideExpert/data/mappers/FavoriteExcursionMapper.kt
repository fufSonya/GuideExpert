package com.example.GuideExpert.data.mappers

import com.example.GuideExpert.data.local.models.ExcursionFavoriteEntity
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteEntity
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteWithData
import com.example.GuideExpert.data.remote.pojo.DeleteFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionFavoritePOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionPOJO
import com.example.GuideExpert.data.remote.pojo.ExcursionsFavoriteResponsePOJO
import com.example.GuideExpert.data.remote.pojo.RestoreFavoriteExcursionResponsePOJO
import com.example.GuideExpert.data.remote.pojo.SetFavoriteExcursionResponsePOJO
import com.example.GuideExpert.domain.models.DeleteFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.ExcursionFavoriteResponse
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.SetFavoriteExcursionResponse

fun ExcursionFavoriteEntity.toExcursionFavorite() = ExcursionFavorite(id=id,excursionId=excursionId,timestamp=timestamp)

fun ExcursionFavorite.toExcursionsFavoriteEntity() =  ExcursionFavoriteEntity(id=id,excursionId=excursionId,timestamp)

fun ExcursionsFavoriteResponsePOJO.toExcursionsFavoriteResponse() = ExcursionFavoriteResponse(success=success,message=message,
    excursions=excursions.map{it.toExcursionFavorite()})

fun ExcursionFavoritePOJO.toExcursionFavorite() = ExcursionFavorite(id = id,excursionId=excursionId,timestamp=timestamp)

fun SetFavoriteExcursionResponsePOJO.toSetFavoriteExcursionResponse() = SetFavoriteExcursionResponse(success=success,message=message,excursion=excursion.toExcursionFavorite())

fun DeleteFavoriteExcursionResponsePOJO.toDeleteFavoriteExcursionResponse() = DeleteFavoriteExcursionResponse(success=success,message=message,excursion=excursion.toExcursionFavorite())

fun ExcursionPOJO.toExcursionsFavoriteWithData() = ExcursionsFavoriteWithData(ExcursionsFavoriteEntity(id = id,title = title, description = description,timestamp = timestamp?: 0), images = images.map{it.toImagePreviewFavoriteEntity()})

fun ExcursionsFavoriteWithData.toExcursionsFavoriteEntity() = ExcursionsFavoriteEntity(id = excursion.id,
    title = excursion.title, description = excursion.description, timestamp = excursion.timestamp, images = images.map { it.toImage() }
)

fun ExcursionsFavoriteWithData.toExcursion() = Excursion(id=excursion.id,title = excursion.title,
    description = excursion.description, images = images.map{it.toImage()}, timestamp = excursion.timestamp)

fun Excursion.toExcursionsFavoriteWithData() = ExcursionsFavoriteWithData(excursion = ExcursionsFavoriteEntity(id = id,title = title,description = description,timestamp=timestamp),images = images.map { it.toImagePreviewFavoriteEntity() })

fun Excursion.toExcursionsFavoriteEntity() = ExcursionsFavoriteEntity(id=id,title = title,description = description,timestamp = timestamp)


fun RestoreFavoriteExcursionResponsePOJO.toRestoreFavoriteExcursionResponse() = RestoreFavoriteExcursionResponse(success=success,message=message,excursion=excursion.toExcursionFavorite())
