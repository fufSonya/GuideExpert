package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.Image
import kotlinx.coroutines.flow.Flow

interface GetImagesExcursionDataUseCase {
    operator fun invoke(excursionId: Int) : Flow<List<Image>>
}