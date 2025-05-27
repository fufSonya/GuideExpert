package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface DeleteAvatarProfileUseCase {
    suspend operator fun invoke() : Flow<UIResources<MessageResponse>>
}