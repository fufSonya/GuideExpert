package com.example.GuideExpert.presentation.ProfileScreen.ProfileMainScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GuideExpert.data.SessionManager
import com.example.GuideExpert.domain.LogoutProfileUseCase
import com.example.GuideExpert.domain.UpdateAvatarProfileUseCase
import com.example.GuideExpert.domain.repository.ProfileRepository
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SnackbarEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val profileRepository: ProfileRepository,
    val logoutProfileUseCase: LogoutProfileUseCase
) : ViewModel() {

    val profileFlow = profileRepository.profileFlow

    val profileStateFlow = profileRepository.profileStateFlow

    private val _effectChannel = Channel<SnackbarEffect>()
    val effectFlow: Flow<SnackbarEffect> = _effectChannel.receiveAsFlow()

    suspend fun sendEffectFlow(message: String, actionLabel: String? = null) {
        _effectChannel.send(SnackbarEffect.ShowSnackbar(message))
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            logoutProfileUseCase()
        }
    }
}