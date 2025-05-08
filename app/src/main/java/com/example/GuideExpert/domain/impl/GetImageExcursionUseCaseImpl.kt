package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetImageExcursionUseCase
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.repository.DataSourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetImageExcursionUseCaseImpl @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
): GetImageExcursionUseCase {
    override operator fun invoke(imageId: Int): Flow<Image> {
        return dataSourceRepository.getImageExcursion(imageId)
    }
}