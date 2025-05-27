package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.ColumnExcursionShimmer
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.FilterBar
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.FilterItem
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.ImageSlider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction2


@Stable
class HomeScreenContentState(
    val filterListState: StateFlow<PagingData<Excursion>>,
    val effectFlow: Flow<SnackbarEffect>,
    val snackbarHostState: SnackbarHostState,
    val onEvent : KFunction1<ExcursionsUiEvent, Unit>,
    val sendEffectFlow : KSuspendFunction2<String, String?, Unit>,
    val navigateToExcursion : (Excursion) -> Unit,
    val getFiltersBar:() -> List<Filter>,
    val profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>,
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    val navigateToProfileInfo: () -> Unit,
    val profile: StateFlow<Profile?>
)

@Composable
fun rememberHomeScreenContentState(
    filterListState: StateFlow<PagingData<Excursion>>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    onEvent: KFunction1<ExcursionsUiEvent, Unit>,
    sendEffectFlow: KSuspendFunction2<String, String?, Unit>,
    navigateToExcursion: (Excursion) -> Unit,
    getFiltersBar:() -> List<Filter>,
    profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    navigateToProfileInfo: () -> Unit,
    profile: StateFlow<Profile?>
): HomeScreenContentState = remember(filterListState,snackbarHostState,onEvent,sendEffectFlow,navigateToExcursion,getFiltersBar,profileFavoriteExcursionIdFlow,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,navigateToProfileInfo,profile) {
    HomeScreenContentState(filterListState,effectFlow,snackbarHostState,onEvent,sendEffectFlow,navigateToExcursion,getFiltersBar,profileFavoriteExcursionIdFlow,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,navigateToProfileInfo,profile)
}

context(SharedTransitionScope)
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    navigateToExcursion: (Excursion) -> Unit,
    toolbarHeightDp: Int,
    filterScreenVisible: Boolean,
    onFiltersSelected: () -> Unit,
    isRefreshing: Boolean,
    stopRefreshing:() -> Unit,
    showTopBar:() -> Unit,
    onScrollingColumn: (Boolean) -> Unit,
    navigateToProfileInfo: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    state: HomeScreenContentState = rememberHomeScreenContentState(
        filterListState = viewModel.uiPagingState,
        effectFlow = viewModel.effectFlow,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
        sendEffectFlow = viewModel::sendEffectFlow,
        navigateToExcursion = navigateToExcursion,
        getFiltersBar = viewModel::getFiltersBar,
        profileFavoriteExcursionIdFlow = viewModel.profileFavoriteExcursionIdFlow,
        stateSetFavoriteExcursion = viewModel.stateSetFavoriteExcursion,
        stateDeleteFavoriteExcursion = viewModel.stateDeleteFavoriteExcursion,
        navigateToProfileInfo = navigateToProfileInfo,
        profile = viewModel.profileFlow
    )
) {

    val effectFlow by state.effectFlow.collectAsStateWithLifecycle(null)

    val filters = state.getFiltersBar()
    val excursionPagingItems by rememberUpdatedState(newValue = state.filterListState.collectAsLazyPagingItems())

    val scope = rememberCoroutineScope()

    if (excursionPagingItems.loadState.append is LoadState.Error) {
        LaunchedEffect(excursionPagingItems.loadState.append) {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        state.snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
    }

    if (excursionPagingItems.loadState.append is LoadState.Error) {
        LaunchedEffect(key1 = excursionPagingItems.loadState.append) {
            scope.launch {
                state.sendEffectFlow(
                    (excursionPagingItems.loadState.append as LoadState.Error).error.message ?: "",
                    null
                )
            }
        }
    }


    if (isRefreshing) {
        excursionPagingItems.refresh()
        stopRefreshing()
    }

    state.ContentSetFavoriteContent(effectFlow)
    state.ContentDeleteFavoriteContent(effectFlow)

    val listState = rememberLazyStaggeredGridState()
    val displayButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 5 } }
    val isCantScrollForwardColumn by remember { derivedStateOf {!listState.canScrollForward && !listState.canScrollBackward} }


    LaunchedEffect (isCantScrollForwardColumn){
        scope.launch {
            if (isCantScrollForwardColumn) { showTopBar() }
            onScrollingColumn(isCantScrollForwardColumn)
        }
    }

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(top = toolbarHeightDp.dp),
        state = listState,
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            ImageSlider(navigateToExcursion = state.navigateToExcursion)
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            FilterBar(
                filters,
                filterScreenVisible = filterScreenVisible,
                onShowFilters = onFiltersSelected,
            )
        }


        if (excursionPagingItems.loadState.refresh is LoadState.Error) {
            item {
                Row(
                    Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(id = R.string.failedload), color = Color.Gray)
                    TextButton({ excursionPagingItems.retry() }) {
                        Text(
                            stringResource(id = R.string.update),
                            fontSize = 15.sp,
                            color = Color.Blue
                        )
                    }
                }
            }
        }

        if (excursionPagingItems.loadState.refresh is LoadState.Loading) {
            items(20) {
                ColumnExcursionShimmer()
            }
        } else {
            if (excursionPagingItems.loadState.source.refresh is LoadState.NotLoading &&
                excursionPagingItems.loadState.append.endOfPaginationReached && excursionPagingItems.itemCount == 0
            ) {
                item {
                    HomeScreenEmpty()
                }
            }
            items(
                count = excursionPagingItems.itemCount,
                key = excursionPagingItems.itemKey { it.id },
            ) { index ->
                val excursion = excursionPagingItems[index]
                if (excursion != null) {
                    state.FilterItem(excursion)
                }
            }
            item {
                if (excursionPagingItems.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            item {
                if (excursionPagingItems.loadState.append is LoadState.Error) {
                    Row(
                        Modifier.padding(start = 15.dp, end = 15.dp, bottom = 150.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(id = R.string.failedload), color = Color.Gray)
                        TextButton({ excursionPagingItems.retry() }) {
                            Text(
                                stringResource(id = R.string.update),
                                fontSize = 15.sp,
                                color = Color.Blue
                            )
                        }
                    }
                }
            }
        }

    }

    if (excursionPagingItems.loadState.append !is LoadState.Error && excursionPagingItems.loadState.refresh !is LoadState.Error) {
        FloatButtonUp(displayButton, listState, showTopBar)
    }

}


@Composable
fun HomeScreenContentState.ContentSetFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateSetFavoriteExcursion by stateSetFavoriteExcursion.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateSetFavoriteExcursion.contentState){
        is SetFavoriteExcursionState.Success -> {
            onEvent(ExcursionsUiEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        is SetFavoriteExcursionState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            onEvent(ExcursionsUiEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }

}

@Composable
fun HomeScreenContentState.ContentDeleteFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateDeleteFavoriteExcursion by stateDeleteFavoriteExcursion.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateDeleteFavoriteExcursion.contentState){
        is DeleteFavoriteExcursionState.Success -> {
            onEvent(ExcursionsUiEvent.OnDeleteFavoriteExcursionStateSetIdle)
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
            onEvent(ExcursionsUiEvent.OnDeleteFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }

}


@Composable
private fun HomeScreenEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(top=10.dp).fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.no_excursions_found),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 27.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FloatButtonUp(displayButton: Boolean, listState : LazyStaggeredGridState, showTopBar: ()->Unit) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(visible = displayButton,
        enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(400)),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(400)),
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
                .fillMaxSize()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = {
                    showTopBar()
                    scope.launch {
                        listState.scrollToItem(0)
                    }
                },
                shape = CircleShape,
            ) {
                Icon(Icons.Filled.KeyboardArrowUp, "Floating action button.")
            }
        }
    }
}