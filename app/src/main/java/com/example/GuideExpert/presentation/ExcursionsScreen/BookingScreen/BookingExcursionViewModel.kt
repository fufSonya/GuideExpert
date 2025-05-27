package com.example.GuideExpert.presentation.ExcursionsScreen.BookingScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GuideExpert.domain.BookingExcursionUseCase
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed class BookingExcursionUiEvent {
    data class OnDateChanged(val date: String) : BookingExcursionUiEvent()
    data class OnTimeChanged(val time: String) : BookingExcursionUiEvent()
    data class OnCountChanged(val count: String) : BookingExcursionUiEvent()
    data class OnEmailChanged(val email: String) : BookingExcursionUiEvent()
    data class OnPhoneChanged(val phone: String) : BookingExcursionUiEvent()
    data class OnCommentsChanged(val comments: String) : BookingExcursionUiEvent()
    data class OnSendRequest(val excursionId: Int) : BookingExcursionUiEvent()
    data object OnBookingExcursionUIStateSetIdle: BookingExcursionUiEvent()
}

data class BookingViewState(
    val count: String = "",
    val email: String = "",
    val phone: String = "",
    val comments: String? = null,
    val date: String? = null,
    val time: String? = null
)

sealed interface BookingExcursionState{
    object Idle:BookingExcursionState
    object Loading:BookingExcursionState
    object Success:BookingExcursionState
    data class Error(val error: String):BookingExcursionState
}

data class BookingExcursionUIState(
    val contentState: BookingExcursionState = BookingExcursionState.Idle
)

@HiltViewModel
class BookingExcursionViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val profileRepository: ProfileRepository,
    val bookingExcursionUseCase: BookingExcursionUseCase,
) : ViewModel() {

    val profileFlow = profileRepository.profileFlow

    private val _bookingViewState: MutableStateFlow<BookingViewState> = MutableStateFlow(
        BookingViewState()
    )
    val viewStateFlow: StateFlow<BookingViewState>
        get() = _bookingViewState

    private val _stateBookingExcursion = MutableStateFlow<BookingExcursionUIState>(BookingExcursionUIState())
    val stateBookingExcursion: StateFlow<BookingExcursionUIState> = _stateBookingExcursion.asStateFlow()

    private val _effectChannel = Channel<SnackbarEffect>()
    val effectFlow: Flow<SnackbarEffect> = _effectChannel.receiveAsFlow()


    fun handleEvent(event: BookingExcursionUiEvent) = viewModelScope.launch {
        when(event) {
            is BookingExcursionUiEvent.OnDateChanged -> {
                setDate(event.date)
            }
            is BookingExcursionUiEvent.OnTimeChanged -> {
                setTime(event.time)
            }
            is BookingExcursionUiEvent.OnCountChanged -> {
                setCount(event.count)
            }

            is BookingExcursionUiEvent.OnEmailChanged -> {
                setEmail(event.email)
            }
            is BookingExcursionUiEvent.OnPhoneChanged -> {
                setPhone(event.phone)
            }

            is BookingExcursionUiEvent.OnCommentsChanged -> {
                setComments(event.comments)
            }

            is BookingExcursionUiEvent.OnSendRequest -> {
                sendRequest(event.excursionId)
            }

            is BookingExcursionUiEvent.OnBookingExcursionUIStateSetIdle -> {
                setIdleBookingExcursionUIState()
            }
        }
    }

    private fun setIdleBookingExcursionUIState() {
        _stateBookingExcursion.update { it.copy(contentState = BookingExcursionState.Idle) }
    }

    private fun setDate(date: String) {
        _bookingViewState.update {
            it.copy(date = date)
        }
    }

    private fun setTime(time: String) {
        _bookingViewState.update {
            it.copy(time = time)
        }
    }

    private fun setCount(count: String) {
        _bookingViewState.update {
            it.copy(count = count)
        }
    }

    private fun setEmail(email: String) {
        _bookingViewState.update {
            it.copy(email = email)
        }
    }

    private fun setPhone(phone: String) {
        _bookingViewState.update {
            it.copy(phone = phone)
        }
    }

    private fun setComments(comments: String) {
        _bookingViewState.update {
            it.copy(comments = comments)
        }
    }

    private fun sendRequest(excursionId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SEND",viewStateFlow.value.toString())
            bookingExcursionUseCase(count = viewStateFlow.value.count,email = viewStateFlow.value.email,
                phone = viewStateFlow.value.phone, comments = viewStateFlow.value.comments!!,date = viewStateFlow.value.date!!,
                time = viewStateFlow.value.time!!, excursionId = excursionId)
                .collectLatest {
                    resources ->
                    when (resources) {
                        is UIResources.Error -> withContext(Dispatchers.Main){
                            _stateBookingExcursion.update {
                                it.copy(
                                    contentState = BookingExcursionState.Error(
                                        resources.message
                                    )
                                )
                            }
                            sendEffectFlow("Error booking excursion : ${resources.message}")
                        }

                        is UIResources.Loading -> withContext(Dispatchers.Main){
                            _stateBookingExcursion.update { it.copy(contentState = BookingExcursionState.Loading) }
                        }

                        is UIResources.Success -> withContext(Dispatchers.Main){
                            _stateBookingExcursion.update { it.copy(contentState = BookingExcursionState.Success) }
                        }
                    }
                }


        }
    }

    suspend fun sendEffectFlow(message: String, actionLabel: String? = null) {
        _effectChannel.send(SnackbarEffect.ShowSnackbar(message))
    }

}