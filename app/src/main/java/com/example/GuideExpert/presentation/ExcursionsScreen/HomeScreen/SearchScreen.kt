package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.LoadingExcursionListShimmer
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.SearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

@Stable
interface SearchStateScope {
    val searchListState: StateFlow<PagingData<Excursion>>
    val stateView: StateFlow<ExcursionsSearchUIState>
    val effectFlow: Flow<SnackbarEffect>
    val snackbarHostState: SnackbarHostState
    val onEvent : (SearchEvent) -> Unit
    val sendEffectFlow : KSuspendFunction2<String, String?, Unit>
    val navigateToExcursion : (Excursion) -> Unit
    val profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>
    val navigateToProfileInfo: () -> Unit
    val profile: StateFlow<Profile?>
}

fun DefaultSearchStateScope(
    searchListState: StateFlow<PagingData<Excursion>>,
    stateView: StateFlow<ExcursionsSearchUIState>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    onEvent: (SearchEvent) -> Unit,
    sendEffectFlow: KSuspendFunction2<String, String?, Unit>,
    navigateToExcursion : (Excursion) -> Unit,
    profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    navigateToProfileInfo: () -> Unit,
    profile: StateFlow<Profile?>
): SearchStateScope {
    return object : SearchStateScope {
        override val searchListState: StateFlow<PagingData<Excursion>>
            get() = searchListState
        override val stateView: StateFlow<ExcursionsSearchUIState>
            get() = stateView
        override val effectFlow: Flow<SnackbarEffect>
            get() = effectFlow
        override val snackbarHostState: SnackbarHostState
            get() = snackbarHostState
        override val onEvent: (SearchEvent) -> Unit
            get() = onEvent
        override val sendEffectFlow: KSuspendFunction2<String, String?, Unit>
            get() = sendEffectFlow
        override val navigateToExcursion: (Excursion) -> Unit
            get() = navigateToExcursion
        override val profileFavoriteExcursionIdFlow: StateFlow<List<ExcursionFavorite>>
            get() = profileFavoriteExcursionIdFlow
        override val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>
            get() = stateSetFavoriteExcursion
        override val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>
            get() = stateDeleteFavoriteExcursion
        override val navigateToProfileInfo: () -> Unit
            get() = navigateToProfileInfo
        override val profile: StateFlow<Profile?>
            get() = profile
    }
}


@Composable
fun rememberDefaultSearchStateScope(
    searchListState: StateFlow<PagingData<Excursion>>,
    stateView: StateFlow<ExcursionsSearchUIState>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    onEvent: (SearchEvent) -> Unit,
    sendEffectFlow: KSuspendFunction2<String, String?, Unit>,
    navigateToExcursion : (Excursion) -> Unit,
    profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    navigateToProfileInfo: () -> Unit,
    profile: StateFlow<Profile?>
): SearchStateScope = remember(searchListState,stateView,snackbarHostState,onEvent,sendEffectFlow,navigateToExcursion,profileFavoriteExcursionIdFlow,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,navigateToProfileInfo,profile) {
    DefaultSearchStateScope(searchListState,stateView,effectFlow,snackbarHostState,onEvent,sendEffectFlow,navigateToExcursion,profileFavoriteExcursionIdFlow,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,navigateToProfileInfo,profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier,
                 snackbarHostState: SnackbarHostState,
                 viewModel: SearchViewModel = hiltViewModel(),
                 navigateToExcursion: (Excursion) -> Unit,
                 scrollingOn:()->Unit,
                 scrollingOff:()->Unit,
                 onActiveChanged: (Boolean) -> Unit,
                 navigateToProfileInfo:()->Unit,
                 scopeState:SearchStateScope = rememberDefaultSearchStateScope(searchListState = viewModel.uiPagingState,
                            stateView = viewModel.stateView,
                            effectFlow = viewModel.effectFlow,
                            snackbarHostState = snackbarHostState,
                            onEvent = viewModel::onEvent,
                            sendEffectFlow = viewModel::sendEffectFlow,
                            navigateToExcursion = navigateToExcursion,
                            profileFavoriteExcursionIdFlow = viewModel.profileFavoriteExcursionIdFlow,
                            stateSetFavoriteExcursion = viewModel.stateSetFavoriteExcursion,
                            stateDeleteFavoriteExcursion = viewModel.stateDeleteFavoriteExcursion,
                            navigateToProfileInfo = navigateToProfileInfo,
                            profile = viewModel.profileFlow
                     ),
                 searchContent: @Composable SearchStateScope.() -> Unit,
){

    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val uiState by scopeState.stateView.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    SearchBar(
        colors = colors(
            dividerColor = MaterialTheme.colorScheme.primary
        ),
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = {
                    text = it
                    scopeState.onEvent(SearchEvent.SetSearchText(text))
                },
                onSearch = { expanded = true
                    coroutineScope.launch { keyboardController?.hide() } },
                expanded = expanded,
                onExpandedChange = {
                    onActiveChanged(it)
                    if(expanded != it)   scopeState.onEvent(SearchEvent.SetStateListSearch(ExcursionListSearchUIState.Idle))
                    expanded = it

                    if (expanded) {
                        scrollingOn()
                    } else {
                        scrollingOff()
                    }
                },
                placeholder = { Text(text = stringResource(R.string.search_str)) },
                leadingIcon = {
                    if (expanded){
                        IconButton(onClick = {
                            expanded = false
                            onActiveChanged(expanded)
                            text = ""
                            scopeState.onEvent(SearchEvent.SetSearchText(text))
                            scopeState.onEvent(SearchEvent.SetStateListSearch(ExcursionListSearchUIState.Idle))
                            scrollingOff()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go Back"
                            )
                        }
                    } else {
                        IconButton(onClick = {}){
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                    } },
                trailingIcon = {
                    if (expanded){
                        IconButton(onClick = {
                            scopeState.onEvent(SearchEvent.SetStateListSearch(ExcursionListSearchUIState.Idle))
                            if (text.isNotEmpty()) {
                                text = ""
                                scopeState.onEvent(SearchEvent.SetSearchText(text))
                            } else {
                                expanded = false
                                onActiveChanged(expanded)
                                scrollingOff()
                            }
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = null,)
                        }
                    }
                }
            )},
        expanded = expanded,
        onExpandedChange = {
            onActiveChanged(it)
            expanded = it
            text = ""
            scopeState.onEvent(SearchEvent.SetSearchText(text))
            if (expanded) {
                scrollingOn()
            } else {
                scrollingOff()
            }},
        modifier = if(expanded){
            Modifier.fillMaxWidth()
        } else{ Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp).fillMaxWidth()},
        windowInsets = if (!expanded)  WindowInsets(0) else SearchBarDefaults.windowInsets
    ) {

        when(uiState.contentState){
            is ExcursionListSearchUIState.Idle -> {
                SearchScreenStart()
            }
            is ExcursionListSearchUIState.Loading -> {}
            is ExcursionListSearchUIState.Data -> {
                scopeState.searchContent()
            }
            is ExcursionListSearchUIState.Error -> {}
        }

    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStateScope.SearchResult() {
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val excursionPagingItems by rememberUpdatedState(newValue = searchListState.collectAsLazyPagingItems())

    val scope = rememberCoroutineScope()

    val listState = rememberLazyStaggeredGridState()
    val displayButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 5 } }


    val effectFlow by effectFlow.collectAsStateWithLifecycle(null)

    if (excursionPagingItems.loadState.append is LoadState.Error) {
        LaunchedEffect(excursionPagingItems.loadState.append) {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
    }

    if (excursionPagingItems.loadState.append is LoadState.Error) {
        LaunchedEffect(key1 = excursionPagingItems.loadState.append) {
            scope.launch {
                sendEffectFlow(
                    (excursionPagingItems.loadState.append as LoadState.Error).error.message ?: "",
                    null
                )
            }
        }
    }

    Log.d("TAG", "itemCount  = ${excursionPagingItems.itemCount.toString()}")

    ContentSetFavoriteContent(effectFlow)
    ContentDeleteFavoriteContent(effectFlow)

    Box(modifier = Modifier.fillMaxSize()) {
        if (excursionPagingItems.loadState.refresh is LoadState.Loading) {
            LoadingExcursionListShimmer()
        } else {

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        excursionPagingItems.refresh()
                        isRefreshing = false
                    }
                },
            ) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Adaptive(300.dp),
                    state = listState
                )
                {

                    if (excursionPagingItems.loadState.refresh is LoadState.Error) {
                        item {
                            Row(Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically){
                                Text(stringResource(id = R.string.failedload),color= Color.Gray)
                                TextButton({ excursionPagingItems.retry()}) {
                                    Text(stringResource(id = R.string.update), fontSize = 15.sp, color = Color.Blue)
                                }
                            }
                        }
                    }

                    if(excursionPagingItems.loadState.source.refresh is LoadState.NotLoading &&
                        excursionPagingItems.loadState.append.endOfPaginationReached && excursionPagingItems.itemCount == 0)
                    {
                        item{
                            SearchScreenEmpty()
                        }
                    }

                    excursionPagingItems.let {
                        items(
                            count = it.itemCount,
                            key = it.itemKey { it.id },
                        ) { index ->
                            val excursion = it[index]
                            if (excursion != null) {
                                SearchItem(excursion,onEvent,navigateToExcursion)
                            }
                        }

                        item {
                            if (it.loadState.append is LoadState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }

                        item {
                            if (it.loadState.append is LoadState.Error) {
                                Row(Modifier.padding(start = 15.dp, end = 15.dp, bottom = 150.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically){
                                    Text(stringResource(id = R.string.failedload),color=Color.Gray)
                                    TextButton({excursionPagingItems.retry()}) {
                                        Text(stringResource(id = R.string.update), fontSize = 15.sp, color = Color.Blue)
                                    }
                                }
                            }
                        }
                    }

                }
                if (excursionPagingItems.loadState.append !is LoadState.Error && excursionPagingItems.loadState.refresh !is LoadState.Error) {
                    FloatButtonUp(displayButton, listState)
                }
            }
        }
    }

}

@Composable
private fun SearchScreenEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(top = 10.dp)) {
        Text(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            text = stringResource(R.string.no_excursions_found),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 27.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SearchScreenStart(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
    }
}

@Composable
private fun FloatButtonUp(displayButton: Boolean, listState : LazyStaggeredGridState) {
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

@Composable
fun SearchStateScope.ContentSetFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateSetFavoriteExcursion by stateSetFavoriteExcursion.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateSetFavoriteExcursion.contentState){
        is SetFavoriteExcursionState.Success -> {
            onEvent(SearchEvent.OnSetFavoriteExcursionStateSetIdle)
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
            onEvent(SearchEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }
}

@Composable
fun SearchStateScope.ContentDeleteFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateDeleteFavoriteExcursion by stateDeleteFavoriteExcursion.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateDeleteFavoriteExcursion.contentState){
        is DeleteFavoriteExcursionState.Success -> {
            onEvent(SearchEvent.OnDeleteFavoriteExcursionStateSetIdle)
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
            onEvent(SearchEvent.OnDeleteFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }

}


