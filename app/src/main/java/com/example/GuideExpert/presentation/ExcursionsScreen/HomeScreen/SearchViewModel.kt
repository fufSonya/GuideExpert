package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.GuideExpert.domain.DeleteFavoriteExcursionUseCase
import com.example.GuideExpert.domain.GetExcursionByQueryUseCase
import com.example.GuideExpert.domain.SetFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SortParams(
    val relevancy: String = "relevancy",
    val popularity: String = "popularity",
)

data class QueryWithFilter(
    val query: String,
    val sort: String
)

sealed interface ExcursionListSearchUIState{
    object Idle:ExcursionListSearchUIState
    object Loading:ExcursionListSearchUIState
    object Data:ExcursionListSearchUIState
    data class Error(val error: String):ExcursionListSearchUIState
}


data class ExcursionsSearchUIState(
    val contentState: ExcursionListSearchUIState = ExcursionListSearchUIState.Idle
)


sealed interface SearchEvent {
    object GetSearchExcursions : SearchEvent
    data class SetSearchText(val text:String) : SearchEvent
    data class SetStateListSearch(val state:ExcursionListSearchUIState) : SearchEvent
    data class OnSetFavoriteExcursion(val excursion: Excursion) : SearchEvent
    data object OnSetFavoriteExcursionStateSetIdle : SearchEvent
    data class OnDeleteFavoriteExcursion(val excursion: Excursion) : SearchEvent
    data object OnDeleteFavoriteExcursionStateSetIdle : SearchEvent
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    val getExcursionByQueryUseCase: GetExcursionByQueryUseCase,
    profileRepository: ProfileRepository,
    excursionsRepository: ExcursionsRepository,
    private val state: SavedStateHandle,
    val setFavoriteExcursionUseCase: SetFavoriteExcursionUseCase,
    val deleteFavoriteExcursionUseCase: DeleteFavoriteExcursionUseCase
) : ViewModel() {
    val profileFavoriteExcursionIdFlow = excursionsRepository.profileFavoriteExcursionIdFlow
    val profileFlow = profileRepository.profileFlow

    private val _uiPagingState = MutableStateFlow<PagingData<Excursion>>(PagingData.empty())
    val uiPagingState: StateFlow<PagingData<Excursion>> = _uiPagingState.asStateFlow()

    private val sortParamsFlow = MutableStateFlow(SortParams().relevancy)

    private val _stateView = MutableStateFlow<ExcursionsSearchUIState>(ExcursionsSearchUIState())
    val stateView: StateFlow<ExcursionsSearchUIState> = _stateView.asStateFlow()

    private val _effectChannel = Channel<SnackbarEffect>()
    val effectFlow: Flow<SnackbarEffect> = _effectChannel.receiveAsFlow()

    private val savedSearchText: StateFlow<String> = state.getStateFlow(key = "QUERY", initialValue = "")

    private val _stateSetFavoriteExcursion = MutableStateFlow<SetFavoriteExcursionUIState>(SetFavoriteExcursionUIState())
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState> = _stateSetFavoriteExcursion.asStateFlow()

    private val _stateDeleteFavoriteExcursion = MutableStateFlow<DeleteFavoriteExcursionUIState>(DeleteFavoriteExcursionUIState())
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState> = _stateDeleteFavoriteExcursion.asStateFlow()

    @OptIn(FlowPreview::class)
    private val currentQueryFlow = savedSearchText
        .debounce(500L)
        .filter { it.isNotEmpty() }
        .onEach {
            onEvent(SearchEvent.SetStateListSearch(ExcursionListSearchUIState.Loading))
        }
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), replay = 1)

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.GetSearchExcursions -> { getSearchExcursions() }
                is SearchEvent.SetSearchText -> { setCurrentText(event.text) }
                is SearchEvent.SetStateListSearch -> { updateExcursionListSearchUIState(event.state) }
                is SearchEvent.OnSetFavoriteExcursion -> setFavoriteExcursion(event.excursion)
                is SearchEvent.OnSetFavoriteExcursionStateSetIdle -> { setIdleSetFavoriteExcursionUIState() }
                is SearchEvent.OnDeleteFavoriteExcursionStateSetIdle -> {setIdleDeleteFavoriteExcursionUIState()}
                is SearchEvent.OnDeleteFavoriteExcursion -> deleteFavoriteExcursion(event.excursion)

            }
        }
    }

    private fun setIdleDeleteFavoriteExcursionUIState() {
        _stateDeleteFavoriteExcursion.update { it.copy(contentState = DeleteFavoriteExcursionState.Idle) }
    }

    private fun setIdleSetFavoriteExcursionUIState() {
        _stateSetFavoriteExcursion.update { it.copy(contentState = SetFavoriteExcursionState.Idle) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getSearchExcursions() {
        combine(
            currentQueryFlow,
            sortParamsFlow,
        ) { (query, sort) ->
            FilterQuery(query, sort)
        }.flowOn(Dispatchers.IO)
            .flatMapLatest {
                getExcursionByQueryUseCase(it)
            }.cachedIn(viewModelScope).collect {
                if(_stateView.value.contentState is ExcursionListSearchUIState.Loading){
                    updateExcursionListSearchUIState(ExcursionListSearchUIState.Data)
                }
                _uiPagingState.value = it
            }
    }

    private fun updateExcursionListSearchUIState(state : ExcursionListSearchUIState) {
        _stateView.update { it.copy(contentState = state) }
    }

    init {
        onEvent(SearchEvent.GetSearchExcursions)
    }

    private fun setCurrentText(query: String) {
        state["QUERY"] = query
     }


    suspend fun sendEffectFlow(message: String, actionLabel: String? = null) {
        _effectChannel.send(SnackbarEffect.ShowSnackbar(message))
    }


     private fun setFavoriteExcursion(excursion: Excursion) {
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


}