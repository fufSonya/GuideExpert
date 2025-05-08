package com.example.GuideExpert.data.repository

import android.util.Log
import com.example.GuideExpert.data.local.DBStorage
import com.example.GuideExpert.data.mappers.toConfig
import com.example.GuideExpert.data.mappers.toExcursionData
import com.example.GuideExpert.data.mappers.toProfileYandex
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.domain.models.Config
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.ProfileYandex
import com.example.GuideExpert.domain.repository.DataSourceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

sealed class UIResources<out T> {
    data class Success<out T>(val data: T) : UIResources<T>()
    data class Error(val message: String) : UIResources<Nothing>()
    data object Loading : UIResources<Nothing>()
}

class DataSourceRepositoryImpl @Inject constructor(
    private val dbStorage : DBStorage,
    private val excursionService: ExcursionService,
): DataSourceRepository {

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

    override suspend fun getConfigInfo(): Flow<UIResources<Config>> = flow<UIResources<Config>> {
        emit(UIResources.Loading)
        val result = excursionService.getConfig()
        if (result.isSuccessful) {
            val config = result.body()?.toConfig() ?: Config()
            emit(UIResources.Success(config))
        }
    }.catch { e->
        emit(UIResources.Error(e.localizedMessage ?: "Unknown error"))
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

    override fun getAuthTokenByYandex(oauthToken:String): Flow<UIResources<ProfileYandex>> = flow<UIResources<ProfileYandex>>{
        emit(UIResources.Loading)
        val result = excursionService.loginYandex(oauthToken)
        if (result.isSuccessful) {
            val profile = result.body()?.toProfileYandex() ?: ProfileYandex()
            emit(UIResources.Success(profile))
        } else {
            emit(UIResources.Error("Unknown error"))
        }
    }.catch { e->
        emit(UIResources.Error(e.localizedMessage ?: "Unknown error"))
    }

    override  fun getExcursionFavoriteFlow(): Flow<List<Excursion>> {
        return dbStorage.getExcursionFavorite().flowOn(Dispatchers.IO)
    }
}