package com.example.GuideExpert.presentation.FavoriteScreen.FavoriteMainScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GuideExpert.domain.DeleteFavoriteExcursionUseCase
import com.example.GuideExpert.domain.GetExcursionFavoriteUseCase
import com.example.GuideExpert.domain.LoadExcursionFavoriteUseCase
import com.example.GuideExpert.domain.RestoreFavoriteExcursionUseCase
import com.example.GuideExpert.domain.SetFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ProfileRepository
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.RestoreFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.RestoreFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionUIState
import com.example.GuideExpert.domain.models.SnackbarEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface LoadFavoritesState{
    object Idle:LoadFavoritesState
    object Loading:LoadFavoritesState
    object Success:LoadFavoritesState
    data class Error(val error: String):LoadFavoritesState
}

data class LoadFavoritesUIState(
    val contentState: LoadFavoritesState = LoadFavoritesState.Idle
)

sealed interface ExcursionsFavoriteUiEvent {
    data object OnLoadExcursionsFavorite : ExcursionsFavoriteUiEvent
    data object OnGetExcursionsFavorite : ExcursionsFavoriteUiEvent
    data object OnLoadFavoritesUIStateSetIdle : ExcursionsFavoriteUiEvent
    data class OnSetFavoriteExcursion(val excursion: Excursion) : ExcursionsFavoriteUiEvent
    data object OnSetFavoriteExcursionStateSetIdle : ExcursionsFavoriteUiEvent
    data class OnDeleteFavoriteExcursion(val excursion: Excursion) : ExcursionsFavoriteUiEvent
    data object OnDeleteFavoriteExcursionStateSetIdle : ExcursionsFavoriteUiEvent
    data class OnRestoreFavoriteExcursion(val excursion: Excursion): ExcursionsFavoriteUiEvent
    data object OnRestoreFavoriteExcursionStateSetIdle : ExcursionsFavoriteUiEvent
}


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val loadExcursionFavoriteUseCase: LoadExcursionFavoriteUseCase,
    val getExcursionFavoriteUseCase: GetExcursionFavoriteUseCase,
    val setFavoriteExcursionUseCase: SetFavoriteExcursionUseCase,
    val deleteFavoriteExcursionUseCase: DeleteFavoriteExcursionUseCase,
    val restoreFavoriteExcursionUseCase: RestoreFavoriteExcursionUseCase,
    private val profileRepository: ProfileRepository,
    ) : ViewModel() {

    private val _excursions = MutableStateFlow(emptyList<Excursion>())
    val excursions = _excursions.asStateFlow()

    val profileFlow = profileRepository.profileFlow

    private val _stateLoadFavorites = MutableStateFlow<LoadFavoritesUIState>(LoadFavoritesUIState())
    val stateLoadFavorites: StateFlow<LoadFavoritesUIState> = _stateLoadFavorites.asStateFlow()

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

    private val _stateRestoreFavoriteExcursion = MutableStateFlow<RestoreFavoriteExcursionUIState>(
        RestoreFavoriteExcursionUIState()
    )
    val stateRestoreFavoriteExcursion: StateFlow<RestoreFavoriteExcursionUIState> = _stateRestoreFavoriteExcursion.asStateFlow()

    fun handleEvent(event: ExcursionsFavoriteUiEvent) {
        viewModelScope.launch {
            when (event) {
                is ExcursionsFavoriteUiEvent.OnLoadExcursionsFavorite -> loadExcursionsFavorite()
                is ExcursionsFavoriteUiEvent.OnLoadFavoritesUIStateSetIdle -> setIdleLoadFavoritesUIState()
                is ExcursionsFavoriteUiEvent.OnDeleteFavoriteExcursion -> {deleteFavoriteExcursion(event.excursion)}
                is ExcursionsFavoriteUiEvent.OnDeleteFavoriteExcursionStateSetIdle -> {setIdleDeleteFavoriteExcursionUIState()}
                is ExcursionsFavoriteUiEvent.OnSetFavoriteExcursion -> {setFavoriteExcursion(event.excursion)}
                is ExcursionsFavoriteUiEvent.OnSetFavoriteExcursionStateSetIdle -> {setIdleSetFavoriteExcursionUIState()}
                is ExcursionsFavoriteUiEvent.OnRestoreFavoriteExcursion -> {restoreFavoriteExcursion(event.excursion)}
                is ExcursionsFavoriteUiEvent.OnRestoreFavoriteExcursionStateSetIdle -> {setIdleRestoreFavoriteExcursionUIState()}
                is ExcursionsFavoriteUiEvent.OnGetExcursionsFavorite -> {getFavoriteExcursion()}
            }
        }
    }

    private suspend fun sendEffectFlow(message: String, actionLabel: String? = null) {
        _effectChannel.send(SnackbarEffect.ShowSnackbar(message))
    }

    private fun setIdleLoadFavoritesUIState() {
        _stateLoadFavorites.update { it.copy(contentState = LoadFavoritesState.Idle) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun loadExcursionsFavorite() {
        profileFlow.onEach { if (it == null || it.id ==0)  handleEvent(ExcursionsFavoriteUiEvent.OnLoadFavoritesUIStateSetIdle)}
            .filter { it != null && it.id !=0 }
            .flatMapLatest {
                loadExcursionFavoriteUseCase()
            }.flowOn(Dispatchers.IO).collectLatest { resources ->
            when(resources) {
                is UIResources.Success -> withContext(Dispatchers.Main){
                    _stateLoadFavorites.update { it.copy(contentState = LoadFavoritesState.Success) }
                }
                is UIResources.Error -> withContext(Dispatchers.Main){
                    _stateLoadFavorites.update {
                        it.copy(
                            contentState = LoadFavoritesState.Error(
                                resources.message
                            )
                        )
                    }
                    sendEffectFlow("Error loading favorites : ${resources.message}")
                }
                is UIResources.Loading -> withContext(Dispatchers.Main){
                    _stateLoadFavorites.update { it.copy(contentState = LoadFavoritesState.Loading) }
                }
            }
        }
    }


    private fun restoreFavoriteExcursion(excursion :Excursion) {
        viewModelScope.launch(Dispatchers.IO) {
            restoreFavoriteExcursionUseCase(excursion).collectLatest { resources ->
                when (resources) {
                    is UIResources.Error -> withContext(Dispatchers.Main){
                        _stateRestoreFavoriteExcursion.update {
                            it.copy(
                                contentState = RestoreFavoriteExcursionState.Error(
                                    resources.message
                                )
                            )
                        }
                        sendEffectFlow("Error restore favorite excursion : ${resources.message}")
                    }

                    is UIResources.Loading -> withContext(Dispatchers.Main){
                        _stateRestoreFavoriteExcursion.update { it.copy(contentState = RestoreFavoriteExcursionState.Loading) }
                    }

                    is UIResources.Success -> withContext(Dispatchers.Main){
                        _stateRestoreFavoriteExcursion.update { it.copy(contentState = RestoreFavoriteExcursionState.Success) }
                    }
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
                        sendEffectFlow("Error insertion favorite excursion : ${resources.message}")
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
                        sendEffectFlow("Error deletion favorite excursion : ${resources.message}")
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

    private fun setIdleRestoreFavoriteExcursionUIState() {
        _stateRestoreFavoriteExcursion.update { it.copy(contentState = RestoreFavoriteExcursionState.Idle) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun  getFavoriteExcursion() {
        viewModelScope.launch {
            profileFlow.filter { it != null && it.id !=0 }
                .distinctUntilChanged()
                .flatMapLatest {
                    getExcursionFavoriteUseCase()
                }.flowOn(Dispatchers.IO).collect { excursions: List<Excursion> ->
                _excursions.update { excursions }
            }
        }
    }

    init{
        handleEvent(ExcursionsFavoriteUiEvent.OnLoadExcursionsFavorite)
        handleEvent(ExcursionsFavoriteUiEvent.OnGetExcursionsFavorite)
    }
}