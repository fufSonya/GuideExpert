package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionDetailUseCase
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExcursionDetailUseCaseImpl @Inject constructor(
    private val excursionsRepository: ExcursionsRepository
): GetExcursionDetailUseCase {

    override suspend operator fun invoke(excursionId: Int): Flow<UIResources<ExcursionData>> {
        return excursionsRepository.getExcursionInfo(excursionId)
    }
}

