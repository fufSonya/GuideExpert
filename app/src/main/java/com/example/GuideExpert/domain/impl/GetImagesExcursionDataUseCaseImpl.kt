package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetImagesExcursionDataUseCase
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetImagesExcursionDataUseCaseImpl @Inject constructor(
    private val excursionsRepository: ExcursionsRepository
): GetImagesExcursionDataUseCase {

    override operator fun invoke(excursionId: Int): Flow<List<Image>> {
        return excursionsRepository.getImagesExcursion(excursionId)
    }
}