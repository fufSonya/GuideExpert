package com.example.GuideExpert.presentation.FavoriteScreen.FavoriteMainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.RestoreFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.RestoreFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SnackbarEffect
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.LoadingExcursionListShimmer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
interface FavoritesScope {
    val handleEvent : (ExcursionsFavoriteUiEvent) -> Unit
    val stateLoadFavorites: StateFlow<LoadFavoritesUIState>
    val effectFlow: Flow<SnackbarEffect>
    val snackbarHostState: SnackbarHostState
    val excursions: Flow<List<Excursion>>
    val navigateToExcursion: (Excursion) -> Unit
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>
    val stateRestoreFavoriteExcursion: StateFlow<RestoreFavoriteExcursionUIState>
    val profile: StateFlow<Profile?>
}

fun DefaultFavoritesScope(
    handleEvent : (ExcursionsFavoriteUiEvent) -> Unit,
    stateLoadFavorites:  StateFlow<LoadFavoritesUIState>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    excursions: Flow<List<Excursion>>,
    navigateToExcursion: (Excursion) -> Unit,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    stateRestoreFavoriteExcursion: StateFlow<RestoreFavoriteExcursionUIState>,
    profile: StateFlow<Profile?>
    ): FavoritesScope {
    return object : FavoritesScope {
        override val handleEvent: (ExcursionsFavoriteUiEvent) -> Unit
            get() = handleEvent
        override val stateLoadFavorites: StateFlow<LoadFavoritesUIState>
            get() = stateLoadFavorites
        override val effectFlow: Flow<SnackbarEffect>
            get() = effectFlow
        override val snackbarHostState:SnackbarHostState
            get() = snackbarHostState
        override val excursions:Flow<List<Excursion>>
            get() = excursions
        override val navigateToExcursion:(Excursion) -> Unit
            get() = navigateToExcursion
        override val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>
            get() = stateSetFavoriteExcursion
        override val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>
            get() = stateDeleteFavoriteExcursion
        override val stateRestoreFavoriteExcursion: StateFlow<RestoreFavoriteExcursionUIState>
            get() = stateRestoreFavoriteExcursion
        override val profile: StateFlow<Profile?>
            get() = profile
    }
}

@Composable
fun rememberDefaultFavoritesScope(
    handleEvent : (ExcursionsFavoriteUiEvent) -> Unit,
    stateLoadFavorites:  StateFlow<LoadFavoritesUIState>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    excursions: Flow<List<Excursion>>,
    navigateToExcursion: (Excursion) -> Unit,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    stateRestoreFavoriteExcursion: StateFlow<RestoreFavoriteExcursionUIState>,
    profile: StateFlow<Profile?>
): FavoritesScope = remember(handleEvent,stateLoadFavorites,effectFlow,snackbarHostState,excursions,navigateToExcursion,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,stateRestoreFavoriteExcursion,profile) {
    DefaultFavoritesScope(handleEvent,stateLoadFavorites,effectFlow,snackbarHostState,excursions,navigateToExcursion,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,stateRestoreFavoriteExcursion,profile)
}


@Composable
fun Favorites(snackbarHostState: SnackbarHostState,
              navigateToExcursion: (Excursion) -> Unit,
              innerPadding: PaddingValues,
              viewModel: FavoritesViewModel = hiltViewModel(),
              scopeState:FavoritesScope = rememberDefaultFavoritesScope(
                  handleEvent = viewModel::handleEvent,
                  stateLoadFavorites = viewModel.stateLoadFavorites,
                  effectFlow = viewModel.effectFlow,
                  snackbarHostState = snackbarHostState,
                  excursions = viewModel.excursions,
                  navigateToExcursion = navigateToExcursion,
                  stateSetFavoriteExcursion = viewModel.stateSetFavoriteExcursion,
                  stateDeleteFavoriteExcursion = viewModel.stateDeleteFavoriteExcursion,
                  stateRestoreFavoriteExcursion = viewModel.stateRestoreFavoriteExcursion,
                  profile = viewModel.profileFlow
              )
)
{
    val stateLoadFavorites by viewModel.stateLoadFavorites.collectAsStateWithLifecycle()
    val effectFlow by scopeState.effectFlow.collectAsStateWithLifecycle(null)
    val profile by scopeState.profile.collectAsStateWithLifecycle(null)

    when(stateLoadFavorites.contentState){
        is LoadFavoritesState.Success -> {
            scopeState.FavoritesDataContent(innerPadding,effectFlow)
        }
        is LoadFavoritesState.Error -> { if (profile == null)  scopeState.FavoritesDataContent(innerPadding,effectFlow) else scopeState.FavoritesDataError(innerPadding,effectFlow) }
        is LoadFavoritesState.Idle -> {FavoritesEmpty(innerPadding)}
        is LoadFavoritesState.Loading -> { scopeState.FavoritesDataLoading(innerPadding) }
    }
}

@Composable
fun FavoritesScope.FavoritesDataError(innerPadding: PaddingValues,effectFlow: SnackbarEffect?) {

    Column (Modifier.padding(innerPadding).fillMaxSize()){
        Row(
            Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.failedload), color = Color.Gray)
            TextButton({
                handleEvent(ExcursionsFavoriteUiEvent.OnLoadExcursionsFavorite)
            }) {
                Text(stringResource(id = R.string.update), fontSize = 15.sp, color = Color.Blue.copy(alpha =.5f))
            }
        }
        Row {
            FavoritesDataContent(innerPadding,effectFlow)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScope.FavoritesDataContent(innerPadding: PaddingValues,effectFlow: SnackbarEffect?) {
    val excursions by excursions.collectAsStateWithLifecycle(null)
    val scope = rememberCoroutineScope()

    ContentDeleteFavoriteContent(effectFlow)
    ContentSetFavoriteContent(effectFlow)
    ContentRestoreFavoriteContent(effectFlow)

    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    excursions?.let {
        PullToRefreshBox(
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = {handleEvent(ExcursionsFavoriteUiEvent.OnLoadExcursionsFavorite)}
        ) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                itemsIndexed(
                    items = it,
                    key = { _, item -> item.id }
                ) { _, excursion ->
                    FavoriteItem(excursion, scope)
                }
            }
        }
        if (it.isEmpty()) FavoritesEmpty(innerPadding)
    }

    if (excursions == null) {
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
            LoadingExcursionListShimmer()
        }
    }
}

@Composable
fun FavoritesScope.ContentDeleteFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateDeleteFavoriteExcursion by stateDeleteFavoriteExcursion.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    when(stateDeleteFavoriteExcursion.contentState){
        is DeleteFavoriteExcursionState.Success -> {
            handleEvent(ExcursionsFavoriteUiEvent.OnDeleteFavoriteExcursionStateSetIdle)
        }
        is DeleteFavoriteExcursionState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            handleEvent(ExcursionsFavoriteUiEvent.OnDeleteFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }
}

@Composable
fun FavoritesScope.FavoritesDataLoading(innerPadding: PaddingValues) {
    Box(modifier = Modifier.padding(innerPadding).fillMaxSize(),
    ) {
        LoadingExcursionListShimmer()
    }
}

@Composable
fun FavoritesScope.ContentSetFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateSetFavoriteExcursion by stateSetFavoriteExcursion.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    when(stateSetFavoriteExcursion.contentState){
        is SetFavoriteExcursionState.Success -> {
            handleEvent(ExcursionsFavoriteUiEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        is SetFavoriteExcursionState.Error -> {
            effectFlow?.let { it1->
                when (it1) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it1.message)
                        }
                    }
                }
            }
            handleEvent(ExcursionsFavoriteUiEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }
}

@Composable
fun FavoritesScope.ContentRestoreFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateRestoreFavoriteExcursion by stateRestoreFavoriteExcursion.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    when(stateRestoreFavoriteExcursion.contentState){
        is RestoreFavoriteExcursionState.Success -> {
            handleEvent(ExcursionsFavoriteUiEvent.OnRestoreFavoriteExcursionStateSetIdle)
        }
        is RestoreFavoriteExcursionState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            handleEvent(ExcursionsFavoriteUiEvent.OnRestoreFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }
}

@Composable
private fun FavoritesEmpty(innerPadding: PaddingValues,modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(innerPadding).fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.not_excursions),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 27.sp,
            textAlign = TextAlign.Center,
        )
    }
}
