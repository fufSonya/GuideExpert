package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.FilterScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.MainTopBar
import com.example.GuideExpert.utils.Constant.STATUSBAR_HEIGHT
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    navigateToExcursion: (Excursion) -> Unit,
    navigateToProfileInfo: () -> Unit,
    innerPadding: PaddingValues
    //  viewModel: ExcursionsViewModel = hiltViewModel()
) {


    val pullToRefreshState = rememberPullToRefreshState()
    val screenHeightDp =  LocalConfiguration.current.screenHeightDp

    var toolbarHeight by rememberSaveable { mutableStateOf(screenHeightDp) }
    var toolbarHeightDp by rememberSaveable { mutableStateOf(toolbarHeight) }
    var toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.dp.roundToPx().toFloat() }


    val localDensity = LocalDensity.current
    var scrolling by rememberSaveable { mutableStateOf(true) }
    var toolbarOffsetHeightPx by rememberSaveable { mutableStateOf(0f) }
    val statusbarHeight =  with(localDensity) { STATUSBAR_HEIGHT.dp.roundToPx().toFloat() }

    var isCantScrollingColumn  by rememberSaveable { mutableStateOf(false) }
    var scrollingColumn by rememberSaveable { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                if(isCantScrollingColumn) {scrollingColumn = false} else {scrollingColumn = true}
                if (scrolling && scrollingColumn && pullToRefreshState.distanceFraction == 0f) toolbarOffsetHeightPx = newOffset.coerceIn(-(toolbarHeightPx+statusbarHeight), 0f)
                return Offset.Zero
            }
        }
    }

    var filtersVisible by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    val padding = if (filtersVisible)  innerPadding else if (scrolling) innerPadding  else PaddingValues(0.dp)
    var defaultModifier = Modifier.padding(padding).fillMaxSize().nestedScroll(nestedScrollConnection)
    if (!filtersVisible) defaultModifier =  defaultModifier.then(Modifier
        .pullToRefresh(
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
        }))

    SharedTransitionLayout {
        Box(Modifier.then(defaultModifier))
        {

            HomeScreenContent(
                snackbarHostState = snackbarHostState,
                navigateToExcursion = navigateToExcursion,
                toolbarHeightDp = toolbarHeightDp,
                filterScreenVisible = filtersVisible,
                onFiltersSelected = {
                    filtersVisible = true
                },
                isRefreshing = isRefreshing,
                stopRefreshing = {
                    coroutineScope.launch {
                          isRefreshing = false
                    }
                },
                showTopBar = {
                    toolbarHeightDp = toolbarHeight
                    toolbarOffsetHeightPx = 0f
                },
                onScrollingColumn = {isCantScrollingColumn = it},
                navigateToProfileInfo = navigateToProfileInfo
            )


            MainTopBar(modifier = Modifier.fillMaxSize(),
                snackbarHostState = snackbarHostState,
                navigateToExcursion = navigateToExcursion,
                navigateToProfileInfo = navigateToProfileInfo,
                toolbarHeightDp = toolbarHeightDp,
                toolbarOffsetHeightPx = toolbarOffsetHeightPx,
                scrollingOn = {
                    toolbarOffsetHeightPx = 0f
                    scrolling = false
                    toolbarHeightDp = screenHeightDp
                },
                scrollingOff = {
                    scrolling = true
                    toolbarHeightDp = toolbarHeight
                },
                onToolbarHeightChange = {
                    toolbarHeight = with(localDensity) { it.toDp() }.value.roundToInt()
                    toolbarHeightDp = toolbarHeight
                    toolbarHeightPx = with(localDensity) { toolbarHeight.dp.roundToPx().toFloat() }
                }
            )

            if (filtersVisible) { scrolling = false}

            AnimatedVisibility(filtersVisible, enter = fadeIn(), exit = fadeOut()) {
                FilterScreen {
                    filtersVisible = false
                    scrolling = true
                }
            }

            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
            )
        }
    }

}