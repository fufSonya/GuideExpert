package com.example.GuideExpert.domain

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.ExcursionData
import kotlinx.coroutines.flow.Flow

interface GetExcursionDetailUseCase {
     suspend operator fun invoke(excursionId: Int) : Flow<UIResources<ExcursionData>>
}