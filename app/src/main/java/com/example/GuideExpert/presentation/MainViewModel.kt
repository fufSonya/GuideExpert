package com.example.GuideExpert.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GuideExpert.data.SessionManager
import com.example.GuideExpert.domain.GetExcursionsFavoriteIdUseCase
import com.example.GuideExpert.domain.GetProfileUseCase
import com.example.GuideExpert.domain.models.ProfileAuthYandexData
import com.example.GuideExpert.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainEvent {
    object GetProfileInfo : MainEvent
    object GetExcursionsFavoriteId: MainEvent
}

@HiltViewModel
class MainViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository,
    private val getProfileUseCase: GetProfileUseCase,
    private val getExcursionsFavoriteIdUseCase: GetExcursionsFavoriteIdUseCase
    ) : ViewModel() {
    val profileId: StateFlow<String> = sessionManager.getProfileId().flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val authToken: StateFlow<String> = sessionManager.getAuthToken().flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val profileFlow = profileRepository.profileFlow

    private suspend fun getProfileInfo() {
        combine(
            profileId,
            authToken,
        ) { (_profileId, _authToken) ->
            val id = if (_profileId.isNotEmpty()) _profileId.toInt() else 0
            ProfileAuthYandexData(id, _authToken)
        }.filter{it.id !=0 && it.authToken !=""}
            .distinctUntilChanged()
            .onEach {
                getProfileUseCase()
                getExcursionsFavoriteIdUseCase()
            }
            .flowOn(Dispatchers.IO)
            .collect {}
    }


    private fun handleEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.GetProfileInfo -> {
                    getProfileInfo()
                }

                is MainEvent.GetExcursionsFavoriteId -> {
                    getIdExcursionsFavorite()
                }
            }
        }
    }

    private suspend fun getIdExcursionsFavorite() {
        profileFlow.filter { it != null && it.id !=0 }
            .distinctUntilChanged()
            .onEach {
                getExcursionsFavoriteIdUseCase()
            }.flowOn(Dispatchers.IO).collectLatest {  }
    }

    init {
        handleEvent(MainEvent.GetProfileInfo)
    }
}


