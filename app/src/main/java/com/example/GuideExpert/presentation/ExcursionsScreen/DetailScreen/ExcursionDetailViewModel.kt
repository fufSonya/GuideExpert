package com.example.GuideExpert.presentation.ExcursionsScreen.DetailScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.DeleteFavoriteExcursionUseCase
import com.example.GuideExpert.domain.GetExcursionDataUseCase
import com.example.GuideExpert.domain.GetExcursionDetailUseCase
import com.example.GuideExpert.domain.GetFiltersGroupsUseCase
import com.example.GuideExpert.domain.GetImagesExcursionDataUseCase
import com.example.GuideExpert.domain.SetFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.repository.ProfileRepository
import com.example.GuideExpert.presentation.ExcursionsScreen.ExcursionDetail
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SearchEvent
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SnackbarEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface ExcursionInfoUIState{
    object Idle:ExcursionInfoUIState
    object Loading:ExcursionInfoUIState
    object Data:ExcursionInfoUIState
    data class Error(val error: String):ExcursionInfoUIState
}

data class UIState(
    val contentState: ExcursionInfoUIState = ExcursionInfoUIState.Idle
)

sealed interface ExcursionDetailUiEvent {
    data object OnLoadExcursionInfo : ExcursionDetailUiEvent
    data class OnSetFavoriteExcursion(val excursion: Excursion) : ExcursionDetailUiEvent
    data object OnSetFavoriteExcursionStateSetIdle : ExcursionDetailUiEvent
    data class OnDeleteFavoriteExcursion(val excursion: Excursion) : ExcursionDetailUiEvent
    data object OnDeleteFavoriteExcursionStateSetIdle : ExcursionDetailUiEvent
}


@HiltViewModel
class ExcursionDetailViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val getExcursionDetailUseCase: GetExcursionDetailUseCase,
    val getExcursionDataUseCase: GetExcursionDataUseCase,
    val getImagesExcursionDataUseCase: GetImagesExcursionDataUseCase,
    val getFiltersGroupsUseCase: GetFiltersGroupsUseCase,
    private val profileRepository: ProfileRepository,
    val setFavoriteExcursionUseCase: SetFavoriteExcursionUseCase,
    val deleteFavoriteExcursionUseCase: DeleteFavoriteExcursionUseCase
) : ViewModel() {

    val excursionDetail = ExcursionDetail.from(savedStateHandle)

    val profileFavoriteExcursionIdFlow = profileRepository.profileFavoriteExcursionIdFlow
    val profileFlow = profileRepository.profileFlow

    private val _stateView = MutableStateFlow<UIState>(UIState())
    val stateView: StateFlow<UIState> = _stateView.asStateFlow()

    val excursion: Flow<ExcursionData?> = getExcursionDataUseCase(excursionDetail.excursion.id)

    val images: Flow<List<Image>> = getImagesExcursionDataUseCase(excursionDetail.excursion.id)

    private val _effectChannel = Channel<SnackbarEffect>()
    val effectFlow: Flow<SnackbarEffect> = _effectChannel.receiveAsFlow()

    private val _stateSetFavoriteExcursion = MutableStateFlow<SetFavoriteExcursionUIState>(
        SetFavoriteExcursionUIState()
    )
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState> = _stateSetFavoriteExcursion.asStateFlow()

    private val _stateDeleteFavoriteExcursion = MutableStateFlow<DeleteFavoriteExcursionUIState>(
        DeleteFavoriteExcursionUIState()
    )
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState> = _stateDeleteFavoriteExcursion.asStateFlow()

    fun getFiltersGroups():List<Filter> {
        return getFiltersGroupsUseCase()
    }

    suspend fun sendEffectFlow(message: String, actionLabel: String? = null) {
        _effectChannel.send(SnackbarEffect.ShowSnackbar(message))
    }

    fun handleEvent(event: ExcursionDetailUiEvent) {
        viewModelScope.launch {
            when (event) {
                is ExcursionDetailUiEvent.OnLoadExcursionInfo -> loadInfo()
                is ExcursionDetailUiEvent.OnDeleteFavoriteExcursion -> {deleteFavoriteExcursion(event.excursion)}
                is ExcursionDetailUiEvent.OnDeleteFavoriteExcursionStateSetIdle -> {setIdleDeleteFavoriteExcursionUIState()}
                is ExcursionDetailUiEvent.OnSetFavoriteExcursion -> {setFavoriteExcursion(event.excursion)}
                is ExcursionDetailUiEvent.OnSetFavoriteExcursionStateSetIdle -> {setIdleSetFavoriteExcursionUIState()}
            }
        }
    }

    private suspend fun loadInfo() {
        getExcursionDetailUseCase(excursionDetail.excursion.id).flowOn(Dispatchers.IO).collectLatest { resource ->
            when(resource) {
                is UIResources.Error -> {
                    _stateView.update { it.copy(contentState = ExcursionInfoUIState.Error(resource.message) ) } }
                is UIResources.Loading -> {
                    _stateView.update { it.copy(contentState = ExcursionInfoUIState.Loading ) } }
                is UIResources.Success -> {
                    _stateView.update { it.copy(contentState = ExcursionInfoUIState.Data ) }
                }
            }
        }
    }

    private fun setFavoriteExcursion(excursion :Excursion) {
        viewModelScope.launch(Dispatchers.IO) {
            setFavoriteExcursionUseCase(excursion).collectLatest { resources ->
                when (resources) {
                    is UIResources.Error -> withContext(Dispatchers.Main){
                        _stateSetFavoriteExcursion.update {
                            it.copy(
                                contentState = SetFavoriteExcursionState.Error(
                                    resources.message
                                )
                            )
                        }
                        sendEffectFlow("Error updating favorite excursion : ${resources.message}")
                    }

                    is UIResources.Loading -> withContext(Dispatchers.Main){
                        _stateSetFavoriteExcursion.update { it.copy(contentState = SetFavoriteExcursionState.Loading) }
                    }

                    is UIResources.Success -> withContext(Dispatchers.Main){
                        _stateSetFavoriteExcursion.update { it.copy(contentState = SetFavoriteExcursionState.Success) }
                    }
                }
            }
        }
    }

    private fun deleteFavoriteExcursion(excursion: Excursion) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavoriteExcursionUseCase(excursion).collectLatest { resources ->
                when (resources) {
                    is UIResources.Error -> withContext(Dispatchers.Main){
                        _stateDeleteFavoriteExcursion.update {
                            it.copy(
                                contentState = DeleteFavoriteExcursionState.Error(
                                    resources.message
                                )
                            )
                        }
                        sendEffectFlow("Error updating favorite excursion : ${resources.message}")
                    }

                    is UIResources.Loading -> withContext(Dispatchers.Main){
                        _stateDeleteFavoriteExcursion.update { it.copy(contentState = DeleteFavoriteExcursionState.Loading) }
                    }

                    is UIResources.Success -> withContext(Dispatchers.Main){
                        _stateDeleteFavoriteExcursion.update { it.copy(contentState = DeleteFavoriteExcursionState.Success) }
                    }
                }
            }
        }
    }

    private fun setIdleDeleteFavoriteExcursionUIState() {
        _stateDeleteFavoriteExcursion.update { it.copy(contentState = DeleteFavoriteExcursionState.Idle) }
    }

    private fun setIdleSetFavoriteExcursionUIState() {
        _stateSetFavoriteExcursion.update { it.copy(contentState = SetFavoriteExcursionState.Idle) }
    }

    init {
        handleEvent(ExcursionDetailUiEvent.OnLoadExcursionInfo)
    }

}