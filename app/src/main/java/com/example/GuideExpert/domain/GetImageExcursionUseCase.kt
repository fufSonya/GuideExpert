package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Image
import kotlinx.coroutines.flow.Flow

interface GetImageExcursionUseCase {
    operator fun invoke(imageId: Int) : Flow<Image>
}