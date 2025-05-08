package com.example.GuideExpert.domain.repository

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.Config
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.ProfileYandex
import kotlinx.coroutines.flow.Flow

interface DataSourceRepository {

    suspend fun getExcursionInfo(excursionId: Int): Flow<UIResources<ExcursionData>>

    suspend fun getConfigInfo(): Flow<UIResources<Config>>

    fun getExcursionData(excursionId: Int): Flow<ExcursionData?>

    fun getImagesExcursion(excursionId: Int): Flow<List<Image>>

    fun getImageExcursion(imageId: Int): Flow<Image>

    fun getAuthTokenByYandex(oauthToken: String): Flow<UIResources<ProfileYandex>>

    fun getExcursionFavoriteFlow(): Flow<List<Excursion>>
}
