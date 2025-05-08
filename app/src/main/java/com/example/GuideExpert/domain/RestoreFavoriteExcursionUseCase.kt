package com.example.GuideExpert.domain

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import kotlinx.coroutines.flow.Flow

interface RestoreFavoriteExcursionUseCase {
    suspend operator fun invoke(excursion: Excursion) : Flow<UIResources<RestoreFavoriteExcursionResponse>>
}