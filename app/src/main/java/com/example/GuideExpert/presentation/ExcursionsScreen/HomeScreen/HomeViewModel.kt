package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.DeleteFavoriteExcursionUseCase
import com.example.GuideExpert.domain.GetConfigUseCase
import com.example.GuideExpert.domain.GetExcursionByFiltersUseCase
import com.example.GuideExpert.domain.GetFiltersBarUseCase
import com.example.GuideExpert.domain.GetFiltersCategoriesUseCase
import com.example.GuideExpert.domain.GetFiltersDurationUseCase
import com.example.GuideExpert.domain.GetFiltersGroupsUseCase
import com.example.GuideExpert.domain.GetFiltersSortUseCase
import com.example.GuideExpert.domain.GetSortDefaultUseCase
import com.example.GuideExpert.domain.SetFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Config
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed interface ExcursionsUiEvent {
    data class OnSetFavoriteExcursion(val excursion: Excursion) : ExcursionsUiEvent
    data class OnDeleteFavoriteExcursion(val excursion: Excursion) : ExcursionsUiEvent
    data object OnLoadExcursionsFilters : ExcursionsUiEvent
    data object OnChangeFilters : ExcursionsUiEvent
    data object OnLoadConfig : ExcursionsUiEvent
    data object OnSetFavoriteExcursionStateSetIdle : ExcursionsUiEvent
    data object OnDeleteFavoriteExcursionStateSetIdle : ExcursionsUiEvent
}

sealed interface SetFavoriteExcursionState{
    object Idle:SetFavoriteExcursionState
    object Loading:SetFavoriteExcursionState
    object Success:SetFavoriteExcursionState
    data class Error(val error: String):SetFavoriteExcursionState
}

data class SetFavoriteExcursionUIState(
    val contentState: SetFavoriteExcursionState = SetFavoriteExcursionState.Idle
)

sealed interface DeleteFavoriteExcursionState{
    object Idle:DeleteFavoriteExcursionState
    object Loading:DeleteFavoriteExcursionState
    object Success:DeleteFavoriteExcursionState
    data class Error(val error: String):DeleteFavoriteExcursionState
}

data class DeleteFavoriteExcursionUIState(
    val contentState: DeleteFavoriteExcursionState = DeleteFavoriteExcursionState.Idle
)

sealed interface RestoreFavoriteExcursionState{
    object Idle:RestoreFavoriteExcursionState
    object Loading:RestoreFavoriteExcursionState
    object Success:RestoreFavoriteExcursionState
    data class Error(val error: String):RestoreFavoriteExcursionState
}

data class RestoreFavoriteExcursionUIState(
    val contentState: RestoreFavoriteExcursionState = RestoreFavoriteExcursionState.Idle
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val getExcursionByFiltersUseCase: GetExcursionByFiltersUseCase,
    val getFiltersBarUseCase: GetFiltersBarUseCase,
    val getFiltersDurationUseCase: GetFiltersDurationUseCase,
    val getFiltersSortUseCase: GetFiltersSortUseCase,
    val getFiltersGroupsUseCase: GetFiltersGroupsUseCase,
    val getFiltersCategoriesUseCase: GetFiltersCategoriesUseCase,
    val getSortDefaultUseCase: GetSortDefaultUseCase,
    val getConfigUseCase: GetConfigUseCase,
    private val profileRepository: ProfileRepository,
    val setFavoriteExcursionUseCase: SetFavoriteExcursionUseCase,
    val deleteFavoriteExcursionUseCase: DeleteFavoriteExcursionUseCase
) : ViewModel() {

    val profileFavoriteExcursionIdFlow = profileRepository.profileFavoriteExcursionIdFlow
    val profileFlow = profileRepository.profileFlow

    private val _effectChannel = Channel<SnackbarEffect>()
    val effectFlow: Flow<SnackbarEffect> = _effectChannel.receiveAsFlow()

    val sortDefault = getSortDefaultUseCase()
    private val _sortState = MutableStateFlow(sortDefault)
    val sortState: StateFlow<Int> = _sortState

    private val _uiPagingState = MutableStateFlow<PagingData<Excursion>>(PagingData.empty())
    val uiPagingState: StateFlow<PagingData<Excursion>> = _uiPagingState.asStateFlow()

    private var oldFilters:Filters = Filters(sortDefault, listOf(), listOf(), listOf())
    private val defaultFilters:Filters = Filters(sortDefault, listOf(), listOf(), listOf())

    private val _changeFilter = MutableStateFlow(defaultFilters)
    val changeFilter: StateFlow<Filters> = _changeFilter

    private val _configApp = MutableStateFlow(Config())
    val configApp: StateFlow<Config> = _configApp

    private val _stateSetFavoriteExcursion = MutableStateFlow<SetFavoriteExcursionUIState>(SetFavoriteExcursionUIState())
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState> = _stateSetFavoriteExcursion.asStateFlow()

    private val _stateDeleteFavoriteExcursion = MutableStateFlow<DeleteFavoriteExcursionUIState>(DeleteFavoriteExcursionUIState())
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState> = _stateDeleteFavoriteExcursion.asStateFlow()


    fun handleEvent(event: ExcursionsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is ExcursionsUiEvent.OnLoadExcursionsFilters -> loadExcursionsFilters()
                is ExcursionsUiEvent.OnSetFavoriteExcursion -> setFavoriteExcursion(event.excursion)
                is ExcursionsUiEvent.OnChangeFilters -> changedFilters()
                is ExcursionsUiEvent.OnLoadConfig -> loadConfig()
                is ExcursionsUiEvent.OnSetFavoriteExcursionStateSetIdle -> {
                    setIdleSetFavoriteExcursionUIState()
                }
                is ExcursionsUiEvent.OnDeleteFavoriteExcursionStateSetIdle -> {setIdleDeleteFavoriteExcursionUIState()}
                is ExcursionsUiEvent.OnDeleteFavoriteExcursion -> deleteFavoriteExcursion(event.excursion)
            }
        }
    }


    private fun setIdleDeleteFavoriteExcursionUIState() {
        _stateDeleteFavoriteExcursion.update { it.copy(contentState = DeleteFavoriteExcursionState.Idle) }
    }

    private fun setIdleSetFavoriteExcursionUIState() {
        _stateSetFavoriteExcursion.update { it.copy(contentState = SetFavoriteExcursionState.Idle) }
    }

    fun getFiltersBar():List<Filter> {
        return getFiltersBarUseCase()
    }

    fun getFiltersDuration():List<Filter> {
        return getFiltersDurationUseCase()
    }

    fun getFiltersSort():List<Filter> {
        return getFiltersSortUseCase()
    }

    fun getFiltersGroups():List<Filter> {
        return getFiltersGroupsUseCase()
    }

    fun getFiltersCategories():List<Filter> {
        return getFiltersCategoriesUseCase()
    }

    private suspend fun loadConfig() {
       getConfigUseCase().flowOn(Dispatchers.IO).collectLatest { resource ->
            when(resource) {
                is UIResources.Success -> {
                    _configApp.update { resource.data }
                }
                else -> {}
            }
        }
    }

    private fun changedFilters() {
        val filters = Filters(sortState.value,
            getFiltersCategories().filter { it.enabled.value}.map{it.id},
            getFiltersDuration().filter { it.enabled.value}.map{it.id},
            getFiltersGroups().filter { it.enabled.value}.map{it.id})
        _changeFilter.update {filters}
    }


    fun resetFilters() {
        _sortState.value = sortDefault
        val filtersBar = getFiltersBar()
        filtersBar.map { it.enabled.value = false }

        val filtersCategories = getFiltersCategories()
        filtersCategories.map { it.enabled.value = false }

        val filtersGroups = getFiltersGroups()
        filtersGroups.map { it.enabled.value = false }

        val filtersDuration = getFiltersDuration()
        filtersDuration.map { it.enabled.value = false }

        val filtersSort = getFiltersSort()
        filtersSort.map { it.enabled.value = false }
    }

    fun isChangedFilters(): Boolean {
        if (oldFilters.sort != sortState.value) return true
        if (oldFilters.categories != getFiltersCategories().filter { it.enabled.value}.map{it.id}) return true
        if (oldFilters.duration != getFiltersDuration().filter { it.enabled.value}.map{it.id}) return true
        if (oldFilters.group != getFiltersGroups().filter { it.enabled.value}.map{it.id}) return true
        return false
    }

    fun isChangedDefaultFilters(): Boolean {
        if (defaultFilters.sort != sortState.value) return true
        if (defaultFilters.categories != getFiltersCategories().filter { it.enabled.value}.map{it.id}) return true
        if (defaultFilters.duration != getFiltersDuration().filter { it.enabled.value}.map{it.id}) return true
        if (defaultFilters.group != getFiltersGroups().filter { it.enabled.value}.map{it.id}) return true
        return false
    }


    fun setOldFilters(filters:Filters) {
        oldFilters = filters
    }
    fun setSortState(sortState:Int) {
        _sortState.update { sortState }
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun loadExcursionsFilters() {
        changeFilter.flatMapLatest{
            getExcursionByFiltersUseCase(changeFilter.value)
        }.cachedIn(viewModelScope)
            .collectLatest{
            _uiPagingState.value = it
        }
    }

    suspend fun sendEffectFlow(message: String, actionLabel: String? = null) {
        _effectChannel.send(SnackbarEffect.ShowSnackbar(message))
    }

    init {
        handleEvent(ExcursionsUiEvent.OnLoadConfig)
        handleEvent(ExcursionsUiEvent.OnLoadExcursionsFilters)
    }


}
