package com.example.GuideExpert.domain

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.Excursion
import kotlinx.coroutines.flow.Flow

interface LoadExcursionFavoriteUseCase {
    suspend operator fun invoke(): Flow<UIResources<List<Excursion>?>>
}