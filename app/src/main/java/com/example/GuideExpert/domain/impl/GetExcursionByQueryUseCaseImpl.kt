package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionByQueryUseCase
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import javax.inject.Inject


class GetExcursionByQueryUseCaseImpl @Inject constructor(
    private val excursionsRepository: ExcursionsRepository
): GetExcursionByQueryUseCase {
    override operator fun invoke(filter: FilterQuery) = excursionsRepository.getExcursionByQueryFlow(filter)
}