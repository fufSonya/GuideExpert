package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.GetExcursionDetailUseCase
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.repository.DataSourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExcursionDetailUseCaseImpl @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
): GetExcursionDetailUseCase {

    override suspend operator fun invoke(excursionId: Int): Flow<UIResources<ExcursionData>> {
        return dataSourceRepository.getExcursionInfo(excursionId)
    }
}

