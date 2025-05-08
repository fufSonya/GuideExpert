package com.example.GuideExpert.data.local

import com.example.GuideExpert.data.local.models.ExcursionsFavoriteWithData
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.Profile
import kotlinx.coroutines.flow.Flow

interface DBStorage {
    fun getExcursionData(excursionId:Int): Flow<ExcursionData?>
    fun getImagesExcursion(excursionId:Int): Flow<List<Image>>
    fun getImageExcursion(imageId:Int): Flow<Image>
    suspend fun insertExcursionInfo(excursion: ExcursionData, images:List<Image>)
    fun getProfile(profileId:Int): Flow<Profile?>
    suspend fun insertProfile(profile: Profile)
    fun getExcursionsFavorite(): Flow<List<ExcursionFavorite>>
    suspend fun insertAllExcursionsFavorite(excursions: List<ExcursionFavorite>)
    suspend fun insertExcursionFavorite(excursion: ExcursionFavorite, excursionUpdate: Excursion)
    suspend fun deleteExcursionFavorite(excursion: ExcursionFavorite, excursionDelete: Excursion)
    suspend fun insertExcursionsFavorite(excursions: List<ExcursionsFavoriteWithData>)
    fun getExcursionFavorite(): Flow<List<Excursion>>
    suspend fun clearAll()
}