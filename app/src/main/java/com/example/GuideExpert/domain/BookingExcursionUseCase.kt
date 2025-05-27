package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface BookingExcursionUseCase {
    suspend operator fun invoke(count: String, email: String, phone: String, comments:String, date: String,time:String, excursionId:Int) : Flow<UIResources<MessageResponse>>
}