package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.BookingExcursionUseCase
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookingExcursionUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val excursionsRepository: ExcursionsRepository
): BookingExcursionUseCase {
    override suspend operator fun invoke(count: String, email: String, phone: String, comments:String, date: String,time:String, excursionId:Int): Flow<UIResources<MessageResponse>> {
        val profile = profileRepository.profileFlow.value
        return excursionsRepository.bookingExcursion(count,email,phone,comments,date,time,excursionId,profile)
    }
}