package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Excursion
import kotlinx.coroutines.flow.Flow

interface GetExcursionFavoriteUseCase {
    operator fun invoke(): Flow<List<Excursion>>
}