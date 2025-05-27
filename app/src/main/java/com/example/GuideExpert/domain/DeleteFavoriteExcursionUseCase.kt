package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.DeleteFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface DeleteFavoriteExcursionUseCase {
    suspend operator fun invoke(excursion: Excursion) : Flow<UIResources<DeleteFavoriteExcursionResponse>>
}