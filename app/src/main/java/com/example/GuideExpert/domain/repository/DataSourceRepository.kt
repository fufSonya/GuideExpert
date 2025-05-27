package com.example.GuideExpert.domain.repository

import com.example.GuideExpert.domain.models.Config
import com.example.GuideExpert.domain.models.ProfileYandex
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface DataSourceRepository {
    suspend fun getConfigInfo(): Flow<UIResources<Config>>
    fun getAuthTokenByYandex(oauthToken: String): Flow<UIResources<ProfileYandex>>
}