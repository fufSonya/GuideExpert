package com.example.GuideExpert.domain.repository

import androidx.paging.PagingData
import com.example.GuideExpert.domain.models.DeleteFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.ErrorExcursionsRepository
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.SetFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ExcursionsRepository {
    val profileFavoriteExcursionIdFlow: StateFlow<List<ExcursionFavorite>>
    suspend fun getExcursionInfo(excursionId: Int): Flow<UIResources<ExcursionData>>
    fun getExcursionData(excursionId: Int): Flow<ExcursionData?>
    fun getImagesExcursion(excursionId: Int): Flow<List<Image>>
    fun getImageExcursion(imageId: Int): Flow<Image>
    fun getExcursionFavoriteFlow(): Flow<List<Excursion>>
    suspend fun getExcursionsFavorite(profile: Profile): ErrorExcursionsRepository?
    suspend fun updateExcursionsFavorite(excursions: List<ExcursionFavorite>)
    suspend fun removeFavoriteExcursionIds()
    suspend fun setFavoriteExcursion(excursion: Excursion,profile: Profile?):Flow<UIResources<SetFavoriteExcursionResponse>>
    suspend fun removeFavoriteExcursion(excursion: Excursion,profile: Profile?):Flow<UIResources<DeleteFavoriteExcursionResponse>>
    suspend fun fetchExcursionsFavorite(profile: Profile?):Flow<UIResources<List<Excursion>?>>
    suspend fun restoreFavoriteExcursion(excursion: Excursion,profile: Profile?):Flow<UIResources<RestoreFavoriteExcursionResponse>>
    fun getExcursionByFiltersFlow(filter: Filters): Flow<PagingData<Excursion>>
    fun getExcursionByQueryFlow(filterQuery: FilterQuery): Flow<PagingData<Excursion>>
    suspend fun bookingExcursion(count: String, email: String, phone: String, comments:String, date: String,time:String, excursionId:Int,profile: Profile?) : Flow<UIResources<MessageResponse>>
}
