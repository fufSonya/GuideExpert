package com.example.GuideExpert.domain

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.Config
import kotlinx.coroutines.flow.Flow

interface GetConfigUseCase {
    suspend operator fun invoke() : Flow<UIResources<Config>>
}