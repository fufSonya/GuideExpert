package com.example.GuideExpert.data.local

import androidx.paging.PagingSource
import com.example.GuideExpert.data.local.models.ExcursionFilterWithData
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteWithData
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.RemoteKey
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

    fun getRemoteKeyById(id:String): Flow<RemoteKey?>
    suspend fun insertExcursionsFilter(excursions: List<ExcursionFilterWithData>, keyId: String, isClearDB:Boolean, nextOffset:Int)
    fun getExcursionsFilter(): PagingSource<Int, ExcursionFilterWithData>

    suspend fun insertExcursionsSearch(excursions: List<ExcursionSearchWithData>, keyId: String, isClearDB:Boolean, nextOffset:Int)
    fun getExcursionsSearch(): PagingSource<Int, ExcursionSearchWithData>

}