package com.example.GuideExpert.presentation.ProfileScreen.YandexActivity

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GuideExpert.data.SessionManager
import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.GetAuthTokenByYandexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileYandexViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val getAuthTokenByYandexUseCase: GetAuthTokenByYandexUseCase,
    val sessionManager: SessionManager,
) : ViewModel() {

    private val _isClosed = MutableStateFlow(false)
    val isClosed: StateFlow<Boolean> = _isClosed.asStateFlow()

    fun loginYandex(oauthToken : String) {
        viewModelScope.launch((Dispatchers.IO)) {
            getAuthTokenByYandexUseCase(oauthToken)
                .collectLatest { resource ->
                    when (resource) {
                        is UIResources.Error -> { _isClosed.update { true } }
                        is UIResources.Loading -> { }
                        is UIResources.Success -> {
                            resource.data.authToken?.let { sessionManager.setAuthToken(it) }
                            resource.data.id?.let { sessionManager.setProfileId(it.toString()) }
                            resource.data.time?.let { sessionManager.setProfileTime(it) }
                            _isClosed.update { true }
                        }
                    }
                }
        }
    }
}
