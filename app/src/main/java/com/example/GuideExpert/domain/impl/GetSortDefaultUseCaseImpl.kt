package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetSortDefaultUseCase
import com.example.GuideExpert.domain.repository.DataProviderRepository
import javax.inject.Inject

class GetSortDefaultUseCaseImpl @Inject constructor(
    private val dataProviderRepository: DataProviderRepository
): GetSortDefaultUseCase {

    override operator fun invoke(): Int {
        return dataProviderRepository.getSortDefault()
    }
}