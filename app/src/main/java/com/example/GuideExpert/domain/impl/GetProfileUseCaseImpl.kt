package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetProfileUseCase
import com.example.GuideExpert.domain.GetSortDefaultUseCase
import com.example.GuideExpert.domain.repository.DataProviderRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): GetProfileUseCase {
    override suspend operator fun invoke() {
        profileRepository.fetchProfile()
    }
}