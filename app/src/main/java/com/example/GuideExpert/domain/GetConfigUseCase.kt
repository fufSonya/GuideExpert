package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Config
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface GetConfigUseCase {
    suspend operator fun invoke() : Flow<UIResources<Config>>
}