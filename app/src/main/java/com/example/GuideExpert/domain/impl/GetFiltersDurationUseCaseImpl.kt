package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetFiltersDurationUseCase
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.repository.DataProviderRepository
import javax.inject.Inject

class GetFiltersDurationUseCaseImpl @Inject constructor(
    private val dataProviderRepository: DataProviderRepository
): GetFiltersDurationUseCase {

    override operator fun invoke(): List<Filter> {
        return dataProviderRepository.getFiltersDuration()
    }
}