package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetFiltersBarUseCase
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.repository.DataProviderRepository
import javax.inject.Inject

class GetFiltersBarUseCaseImpl @Inject constructor(
    private val dataProviderRepository: DataProviderRepository
): GetFiltersBarUseCase {

    override operator fun invoke(): List<Filter> {
        return dataProviderRepository.getFiltersBar()
    }
}