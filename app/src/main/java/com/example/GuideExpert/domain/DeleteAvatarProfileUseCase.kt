package com.example.GuideExpert.domain

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.MessageResponse
import kotlinx.coroutines.flow.Flow

interface DeleteAvatarProfileUseCase {
    suspend operator fun invoke() : Flow<UIResources<MessageResponse>>
}