package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.LogoutProfileUseCase
import com.example.GuideExpert.domain.repository.ProfileRepository
import javax.inject.Inject

class LogoutProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): LogoutProfileUseCase {
    override suspend operator fun invoke() {
        profileRepository.removeProfile()
    }
}