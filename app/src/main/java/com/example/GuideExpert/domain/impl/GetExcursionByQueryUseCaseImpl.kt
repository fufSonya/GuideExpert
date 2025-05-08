package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionByQueryUseCase
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.repository.DataPagingRepository
import javax.inject.Inject


class GetExcursionByQueryUseCaseImpl @Inject constructor(
    private val dataPagingRepository: DataPagingRepository
): GetExcursionByQueryUseCase {
    override operator fun invoke(filter: FilterQuery) = dataPagingRepository.getExcursionByQueryFlow(filter)
}