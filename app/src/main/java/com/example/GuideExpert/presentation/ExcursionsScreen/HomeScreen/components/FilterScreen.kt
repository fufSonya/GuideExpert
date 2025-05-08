package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.FilterType
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.ExcursionsUiEvent
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.HomeViewModel
import com.example.GuideExpert.ui.theme.Shadow1
import kotlinx.coroutines.flow.StateFlow


@Stable
class FilterState(
    val sortState:  StateFlow<Int>,
    val setSortState: (Int) -> Unit,
    val handleEvent: (ExcursionsUiEvent) -> Unit,
    val setOldFilters:(Filters) -> Unit,
    val isChangedFilters:() -> Boolean,
    val isChangedDefaultFilters:() -> Boolean,
    val resetFilters:() -> Unit,
    val getFiltersBar:() -> List<Filter>,
    val getFiltersDuration: () -> List<Filter>,
    val getFiltersSort: () -> List<Filter>,
    val getFiltersGroups: () -> List<Filter>,
    val getFiltersCategories: () -> List<Filter>
)

@Composable
fun rememberFilterState(
    sortState:  StateFlow<Int>,
    setSortState: (Int) -> Unit,
    handleEvent: (ExcursionsUiEvent) -> Unit,
    setOldFilters:(Filters) -> Unit,
    isChangedFilters:() -> Boolean,
    isChangedDefaultFilters:() -> Boolean,
    resetFilters:() -> Unit,
    getFiltersBar:() -> List<Filter>,
    getFiltersDuration: () -> List<Filter>,
    getFiltersSort: () -> List<Filter>,
    getFiltersGroups: () -> List<Filter>,
    getFiltersCategories: () -> List<Filter>
): FilterState = remember(sortState,setSortState,
    handleEvent,setOldFilters,isChangedFilters,isChangedDefaultFilters,resetFilters,
    getFiltersBar,getFiltersDuration,getFiltersSort,getFiltersGroups,getFiltersCategories) {
    FilterState(sortState,setSortState,handleEvent,setOldFilters,isChangedFilters,
        isChangedDefaultFilters,resetFilters,getFiltersBar,getFiltersDuration,getFiltersSort,
        getFiltersGroups,getFiltersCategories)
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FilterScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    state: FilterState = rememberFilterState(
        sortState = viewModel.sortState,
        setSortState = viewModel::setSortState,
        handleEvent = viewModel::handleEvent,
        setOldFilters = viewModel::setOldFilters,
        isChangedFilters = viewModel::isChangedFilters,
        isChangedDefaultFilters = viewModel::isChangedDefaultFilters,
        resetFilters = viewModel::resetFilters,
        getFiltersBar= viewModel::getFiltersBar,
        getFiltersDuration = viewModel::getFiltersDuration,
        getFiltersSort = viewModel::getFiltersSort,
        getFiltersGroups = viewModel::getFiltersGroups,
        getFiltersCategories = viewModel::getFiltersCategories
        ),
    onDismiss: () -> Unit
) {
    val sortState = state.sortState.collectAsState()

    LaunchedEffect(Unit) {
        state.setOldFilters(
            Filters(sortState.value,
                if (state.getFiltersCategories().isNotEmpty()) state.getFiltersCategories().filter {  it.enabled.value  }.map{it.id}
                else listOf(),
                if (state.getFiltersDuration().isNotEmpty()) state.getFiltersDuration().filter {  it.enabled.value  }.map{it.id}
                else listOf(),
                if (state.getFiltersGroups().isNotEmpty()) state.getFiltersGroups().filter {  it.enabled.value  }.map{it.id}
                else listOf()
            )
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // capture click
            }
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (state.isChangedFilters()) state.handleEvent(ExcursionsUiEvent.OnChangeFilters)
                    onDismiss()
                }
        )

        Column(
            Modifier
                .padding(16.dp)
                .align(Alignment.Center)
                .clip(MaterialTheme.shapes.medium)
                .sharedBounds(
                    rememberSharedContentState(FilterSharedElementKey),
                    animatedVisibilityScope = this@AnimatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium)
                )
                .wrapContentSize()
                .heightIn(max = 450.dp)
                .verticalScroll(rememberScrollState())
                /*.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }*/
                .background(MaterialTheme.colorScheme.onSecondary)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .skipToLookaheadSize(),
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = { if(state.isChangedFilters()) state.handleEvent(ExcursionsUiEvent.OnChangeFilters)
                    onDismiss()}) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = ""
                    )
                }
                Text(
                    text = stringResource(id = R.string.label_filters),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 8.dp, end = 48.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                val resetEnabled = state.isChangedDefaultFilters()

                IconButton(
                    onClick = { state.resetFilters() },
                    enabled = resetEnabled
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "",
                        tint = Shadow1.copy(alpha = if (!resetEnabled) 0.38f else 1f)
                    )

                }

            }

            with(state) {
                SortFiltersSection(
                    sortState = sortState.value,
                    onFilterChange = { filter ->
                        state.setSortState(filter.id)
                    }
                )
            }

            FilterChipSection(
                title = stringResource(id = R.string.categories),
                filters = state.getFiltersCategories()
            )

            FilterChipSection(
                title = stringResource(id = R.string.groups),
                filters = state.getFiltersGroups()
            )

            FilterChipSection(
                title = stringResource(id = R.string.duration),
                filters = state.getFiltersDuration()
            )
        }
    }
}

context(FilterState)
@Composable
fun SortFiltersSection(sortState: Int, onFilterChange: (Filter) -> Unit) {
    FilterTitle(text = stringResource(id = R.string.sort))
    Column(Modifier.padding(bottom = 24.dp)) {
        SortFilters(
            sortState = sortState,
            onChanged = onFilterChange
        )
    }
}

@Composable
fun FilterTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = Shadow1,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

context(FilterState)
@Composable
fun SortFilters(
    sortState: Int,
    onChanged: (Filter) -> Unit
) {

    getFiltersBar().filter{  it.type == FilterType.Sort }.map{ it.enabled.value = false }

    getFiltersSort().forEach { filter ->
        SortOption(
            text = filter.name,
            icon = filter.icon,
            selected = sortState == filter.id,
            onClickOption = {
                onChanged(filter)
            }
        )
    }
}

@Composable
fun SortOption(
    text: String,
    icon: ImageVector?,
    onClickOption: () -> Unit,
    selected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(top = 14.dp)
            .selectable(selected) { onClickOption() }
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = Shadow1
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChipSection(title: String, filters: List<Filter>) {
    FilterTitle(text = title)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)
            .padding(horizontal = 4.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                filter = filter,
                modifier = Modifier.padding(end = 4.dp, bottom = 8.dp),
                shape = CircleShape,
                isFilterScreen = true
            )
        }
    }
}
