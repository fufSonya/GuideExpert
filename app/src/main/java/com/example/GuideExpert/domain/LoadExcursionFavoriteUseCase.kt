package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface LoadExcursionFavoriteUseCase {
    suspend operator fun invoke(): Flow<UIResources<List<Excursion>?>>
}