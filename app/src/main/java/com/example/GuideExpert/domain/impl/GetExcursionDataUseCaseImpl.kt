package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionDataUseCase
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetExcursionDataUseCaseImpl @Inject constructor(
    private val excursionsRepository: ExcursionsRepository
): GetExcursionDataUseCase {

    override operator fun invoke(excursionId: Int): Flow<ExcursionData?> {
        return excursionsRepository.getExcursionData(excursionId)
    }
}
