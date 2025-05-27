package com.example.GuideExpert.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.GuideExpert.data.ExcursionFiltersRemoteMediator
import com.example.GuideExpert.data.ExcursionSearchRemoteMediator
import com.example.GuideExpert.data.local.DBStorage
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteWithData
import com.example.GuideExpert.data.mappers.toDeleteFavoriteExcursionResponse
import com.example.GuideExpert.data.mappers.toExcursion
import com.example.GuideExpert.data.mappers.toExcursionData
import com.example.GuideExpert.data.mappers.toExcursionsFavoriteResponse
import com.example.GuideExpert.data.mappers.toExcursionsFavoriteWithData
import com.example.GuideExpert.data.mappers.toMessageResponse
import com.example.GuideExpert.data.mappers.toRestoreFavoriteExcursionResponse
import com.example.GuideExpert.data.mappers.toSetFavoriteExcursionResponse
import com.example.GuideExpert.data.mappers.toUpdateProfileResponse
import com.example.GuideExpert.data.remote.services.ExcursionAuthService
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.domain.models.DeleteFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.ErrorExcursionsRepository
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.ExcursionFavoriteResponse
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.SetFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import com.example.GuideExpert.utils.Constant.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class ExcursionsRepositoryImpl @Inject constructor(
    private val dbStorage : DBStorage,
    private val excursionService: ExcursionService,
    private val excursionAuthService: ExcursionAuthService
): ExcursionsRepository {

    private val _profileFavoriteExcursionIdFlow = MutableStateFlow<List<ExcursionFavorite>>(listOf())
    override val profileFavoriteExcursionIdFlow: StateFlow<List<ExcursionFavorite>> get() = _profileFavoriteExcursionIdFlow

    @OptIn(ExperimentalPagingApi::class)
    override fun getExcursionByFiltersFlow(filter: Filters): Flow<PagingData<Excursion>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, maxSize = 30),
            remoteMediator = ExcursionFiltersRemoteMediator(
                excursionService = excursionService,
                filters = filter,
                dbStorage = dbStorage
            ),
            pagingSourceFactory = {
                dbStorage.getExcursionsFilter()
            },
        ).flow.map { pagingData -> pagingData.map { it.toExcursion() } }
            .flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getExcursionByQueryFlow(filterQuery: FilterQuery): Flow<PagingData<Excursion>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, maxSize = 30),
            remoteMediator = ExcursionSearchRemoteMediator(
                excursionService = excursionService,
                filterQuery = filterQuery,
                dbStorage = dbStorage
            ),
            pagingSourceFactory = {
                dbStorage.getExcursionsSearch()
            },
        ).flow.map { pagingData -> pagingData.map { it.toExcursion() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun bookingExcursion(
        count: String,
        email: String,
        phone: String,
        comments: String,
        date: String,
        time: String,
        excursionId: Int,
        profile: Profile?
    ): Flow<UIResources<MessageResponse>> = flow {
        try {
            emit(UIResources.Loading)
            if (profile == null){
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            val result = excursionAuthService.bookingExcursion(profile.id, excursionId,count,email,phone,comments,date,time)
            if (result.code() == 403) {
                emit(UIResources.Error("Error authorization"))
                return@flow
            }

            if (result.isSuccessful) {
                val response =
                    result.body()?.toMessageResponse() ?: MessageResponse()
                if (response.success) {
                    emit(UIResources.Success(response))
                } else {
                    emit(UIResources.Error("Error :: ${response.message}"))
                }
            } else {
                emit(UIResources.Error("Error remove favorite"))
            }

        }catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }

    }

    override suspend fun getExcursionInfo(excursionId:Int): Flow<UIResources<ExcursionData>> = flow {
       try {
           emit(UIResources.Loading)
           val result = excursionService.getExcursionData(excursionId)
           val data = result.body()?.toExcursionData() ?: ExcursionData()
           emit(UIResources.Success(data))
           dbStorage.insertExcursionInfo(data, data.images)
       } catch (e: IOException) {
           emit(UIResources.Error(e.message.toString()))
       }
    }


    override fun getExcursionData(excursionId:Int): Flow<ExcursionData?> {
        return dbStorage.getExcursionData(excursionId).flowOn(Dispatchers.IO)
    }

    override fun getImagesExcursion(excursionId:Int): Flow<List<Image>> {
        return dbStorage.getImagesExcursion(excursionId).flowOn(Dispatchers.IO)
    }

    override fun getImageExcursion(imageId:Int): Flow<Image> {
        return dbStorage.getImageExcursion(imageId).flowOn(Dispatchers.IO)
    }

    override  fun getExcursionFavoriteFlow(): Flow<List<Excursion>> {
        return dbStorage.getExcursionFavorite().flowOn(Dispatchers.IO)
    }

    override suspend fun getExcursionsFavorite(profile: Profile): ErrorExcursionsRepository? {
        try {
            val localExcursionsFavoriteId = dbStorage.getExcursionsFavorite().firstOrNull()
            if (localExcursionsFavoriteId != null) {
                _profileFavoriteExcursionIdFlow.update { localExcursionsFavoriteId }
            }
            val result = excursionAuthService.getExcursionsFavoriteId(profile.id)
            if (result.code() == 403) {
                return ErrorExcursionsRepository.Authorization
            }
            if (result.isSuccessful) {
                val response =
                    result.body()?.toExcursionsFavoriteResponse() ?: ExcursionFavoriteResponse()
                if (response.success) {
                    updateExcursionsFavorite(response.excursions)
                } else {
                    Log.d("TAG", "Error loading favorite")
                    return ErrorExcursionsRepository.LoadingFavorites
                }
            } else {
                Log.d("TAG", "Error loading favorite")
                return ErrorExcursionsRepository.LoadingFavorites
            }
        }catch (_: Exception) {
            Log.d("TAG", "Error loading favorite")
            return ErrorExcursionsRepository.LoadingFavorites
        }
        return null
    }

    override suspend fun updateExcursionsFavorite(excursions: List<ExcursionFavorite>) {
        _profileFavoriteExcursionIdFlow.update { excursions }
        dbStorage.insertAllExcursionsFavorite(excursions)
    }

    override suspend fun removeFavoriteExcursionIds() {
        _profileFavoriteExcursionIdFlow.update { listOf() }
    }

    override suspend fun setFavoriteExcursion(excursion: Excursion,profile: Profile?): Flow<UIResources<SetFavoriteExcursionResponse>> = flow {
        try {
            emit(UIResources.Loading)
            if (profile == null){
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            val result = excursionAuthService.setExcursionFavorite(profile.id,excursion.id)
            if (result.code() == 403) {
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            if (result.isSuccessful) {
                val response =
                    result.body()?.toSetFavoriteExcursionResponse() ?: SetFavoriteExcursionResponse()
                if (response.success) {
                    val excursionUpdate = excursion.copy(timestamp = response.excursion?.timestamp ?:0)
                    dbStorage.insertExcursionFavorite(response.excursion!!,excursionUpdate)
                    _profileFavoriteExcursionIdFlow.update { it + response.excursion }
                    emit(UIResources.Success(response))
                } else {
                    emit(UIResources.Error("Error :: ${response.message}"))
                }
            } else {
                emit(UIResources.Error("Error set favorite"))
            }
        } catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }
    }

    override suspend fun removeFavoriteExcursion(excursion: Excursion,profile: Profile?): Flow<UIResources<DeleteFavoriteExcursionResponse>> = flow{
        try {
            emit(UIResources.Loading)
            if (profile == null){
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            val result = excursionAuthService.removeExcursionFavorite(profile.id,excursion.id)
            if (result.code() == 403) {
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            if (result.isSuccessful) {
                val response =
                    result.body()?.toDeleteFavoriteExcursionResponse() ?: DeleteFavoriteExcursionResponse()
                if (response.success) {
                    dbStorage.deleteExcursionFavorite(response.excursion!!,excursion)
                    _profileFavoriteExcursionIdFlow.update {it.filter { it1 -> it1.excursionId != response.excursion.excursionId } }
                    emit(UIResources.Success(response))
                } else {
                    emit(UIResources.Error("Error :: ${response.message}"))
                }
            } else {
                emit(UIResources.Error("Error remove favorite"))
            }
        } catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }
    }

    override suspend fun fetchExcursionsFavorite(profile: Profile?): Flow<UIResources<List<Excursion>?>> = flow  {
        try{
            emit(UIResources.Loading)
            if (profile == null){
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            val result = excursionAuthService.getExcursionsFavorite(profile.id)
            if (result.code() == 403) {
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            if (result.isSuccessful) {
                val response = result.body()?.excursions ?.map { it.toExcursionsFavoriteWithData() } ?: listOf<ExcursionsFavoriteWithData>()
                dbStorage.insertExcursionsFavorite(response)
                emit(UIResources.Success( result.body()?.excursions ?.map { it.toExcursion()}))
            } else {
                emit(UIResources.Error("Error fetch favorites"))
            }
        } catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }
    }

    override suspend fun restoreFavoriteExcursion(excursion: Excursion,profile: Profile?): Flow<UIResources<RestoreFavoriteExcursionResponse>> = flow {
        try {
            emit(UIResources.Loading)
            if (profile == null){
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            val result = excursionAuthService.restoreExcursionFavorite(profile.id,excursion.id,excursion.timestamp)
            if (result.code() == 403) {
                emit(UIResources.Error("Error authorization"))
                return@flow
            }
            if (result.isSuccessful) {
                val response =
                    result.body()?.toRestoreFavoriteExcursionResponse() ?: RestoreFavoriteExcursionResponse()
                if (response.success) {
                    val excursionUpdate = excursion.copy(timestamp = response.excursion?.timestamp ?:0)
                    dbStorage.insertExcursionFavorite(response.excursion!!,excursionUpdate)
                    _profileFavoriteExcursionIdFlow.update { it + response.excursion }
                    emit(UIResources.Success(response))
                } else {
                    emit(UIResources.Error("Error :: ${response.message}"))
                }
            } else {
                emit(UIResources.Error("Error restore favorite"))
            }
        } catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }
    }

}