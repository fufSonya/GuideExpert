package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface GetExcursionDetailUseCase {
     suspend operator fun invoke(excursionId: Int) : Flow<UIResources<ExcursionData>>
}