package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UpdateProfileUseCase {
    suspend operator fun invoke(firstName: String, lastName: String, sex: String?, email:String, birthday: Date) : Flow<UIResources<MessageResponse>>
}