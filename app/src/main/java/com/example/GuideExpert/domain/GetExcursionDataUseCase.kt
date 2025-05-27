package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.ExcursionData
import kotlinx.coroutines.flow.Flow

interface GetExcursionDataUseCase {
    operator fun invoke(excursionId: Int) : Flow<ExcursionData?>
}