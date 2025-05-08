package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.FilterType
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.ExcursionsUiEvent
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.HomeViewModel
import com.example.GuideExpert.ui.theme.Shadow1
import com.example.GuideExpert.ui.theme.Shadow2
import kotlinx.coroutines.flow.StateFlow

fun Modifier.fadeInDiagonalGradientBorder(
    showBorder: Boolean,
    colors: List<Color>,
    borderSize: Dp = 2.dp,
    shape: Shape
) = composed {
    val animatedColors = List(colors.size) { i ->
        animateColorAsState(
            if (showBorder) colors[i] else colors[i].copy(alpha = 0f),
            label = "animated color"
        ).value
    }
    diagonalGradientBorder(
        colors = animatedColors,
        borderSize = borderSize,
        shape = shape
    )
}

fun Modifier.diagonalGradientBorder(
    colors: List<Color>,
    borderSize: Dp = 2.dp,
    shape: Shape
) = border(
    width = borderSize,
    brush = Brush.linearGradient(colors),
    shape = shape
)

fun Modifier.offsetGradientBackground(
    colors: List<Color>,
    width: Float,
    offset: Float = 0f
) = background(
    Brush.horizontalGradient(
        colors = colors,
        startX = -offset,
        endX = width - offset,
        tileMode = TileMode.Mirror
    )
)

object FilterSharedElementKey

@Stable
class FilterChipState(
    val sortState: StateFlow<Int>,
    val setSortState: (Int) -> Unit,
    val handleEvent: (ExcursionsUiEvent) -> Unit,
    val getFiltersBar:() -> List<Filter>,
    val getFiltersDuration: () -> List<Filter>,
    val getFiltersSort: () -> List<Filter>,
    val getFiltersGroups: () -> List<Filter>,
    val getFiltersCategories: () -> List<Filter>,
    val sortDefault: Int
)

@Composable
fun rememberFilterChipState(
    sortState: StateFlow<Int>,
    setSortState: (Int) -> Unit,
    handleEvent: (ExcursionsUiEvent) -> Unit,
    getFiltersBar:() -> List<Filter>,
    getFiltersDuration:() -> List<Filter>,
    getFiltersSort:() -> List<Filter>,
    getFiltersGroups: () -> List<Filter>,
    getFiltersCategories: () -> List<Filter>,
    sortDefault: Int
): FilterChipState = remember(sortState,setSortState,handleEvent,getFiltersBar,getFiltersDuration,
    getFiltersSort,getFiltersGroups,getFiltersCategories,sortDefault) {
    FilterChipState(sortState,setSortState,handleEvent,getFiltersBar,getFiltersDuration,
        getFiltersSort,getFiltersGroups,getFiltersCategories,sortDefault)
}

context(SharedTransitionScope)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FilterBar(
    filters: List<Filter>,
    onShowFilters: () -> Unit,
    filterScreenVisible: Boolean,
) {

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
        modifier = Modifier.heightIn(min = 56.dp)
    ) {
        item {
            AnimatedVisibility(visible = !filterScreenVisible) {
                IconButton(
                    onClick = onShowFilters,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(FilterSharedElementKey),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        tint = Shadow1,
                        contentDescription = "",
                        modifier = Modifier.diagonalGradientBorder(
                            colors = listOf(Shadow1, Shadow2),
                            shape = CircleShape
                        )
                    )
                }
            }
        }
        items(filters) { filter ->
            FilterChip(filter = filter, shape = CircleShape)
        }
    }
}

@Composable
fun FilterChip(
    filter: Filter,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    isFilterScreen : Boolean = false,
    viewModel: HomeViewModel = hiltViewModel(),
    state: FilterChipState = rememberFilterChipState(
        sortState = viewModel.sortState,
        setSortState = viewModel::setSortState,
        handleEvent = viewModel::handleEvent,
        getFiltersBar = viewModel::getFiltersBar,
        getFiltersDuration = viewModel::getFiltersDuration,
        getFiltersSort = viewModel::getFiltersSort,
        getFiltersGroups = viewModel::getFiltersGroups,
        getFiltersCategories = viewModel::getFiltersCategories,
        sortDefault = viewModel::sortDefault.get()
        ),
) {
    val sortState = state.sortState.collectAsState()
    val selected = filter.enabled
    if (filter.type == FilterType.Sort) {
        if (sortState.value == filter.id) selected.value = true
    }

    val backgroundColor by animateColorAsState(
        if (selected.value) Shadow1 else MaterialTheme.colorScheme.background,
        label = "background color"
    )
    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected.value,
        colors = listOf(Shadow1, Shadow2),
        shape = shape
    )
    val textColor by animateColorAsState(
        if (selected.value) Color.Black else Color.White,
        label = "text color"
    )

    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        elevation = 2.dp
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed) {
                Modifier.offsetGradientBackground(
                    listOf(Shadow1, Shadow2),
                    200f,
                    0f
                )
            } else {
                Modifier.background(Color.Transparent)
            }
        Box(
            modifier = Modifier
                .toggleable(
                    value = selected.value,
                    onValueChange = {state.setFilterScreen(filter,it)
                        if(!isFilterScreen) state.handleEvent(ExcursionsUiEvent.OnChangeFilters)},
                    interactionSource = interactionSource,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border),
        ) {
            Text(
                text = filter.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                ),
                fontWeight = FontWeight.Bold,
                color = if (selected.value) Color.White else MaterialTheme.typography.labelMedium.color
            )
        }
    }
}

fun FilterChipState.setFilterScreen(filter: Filter, enabled: Boolean) {
    getFiltersBar().filter { it.type == filter.type &&  it.id == filter.id }
        .map { it.enabled.value = enabled }

    var filters = listOf<Filter>()
    when (filter.type) {
        is FilterType.Duration -> filters = getFiltersDuration()
        is FilterType.Sort -> {
            filters = getFiltersSort()
            if (enabled) setSortState(filter.id) else setSortState(sortDefault)
        }
        is FilterType.Groups -> filters = getFiltersGroups()
        is FilterType.Categories -> filters = getFiltersCategories()
    }

    filters.filter { it.type == filter.type &&  it.id == filter.id }
        .map { it.enabled.value = enabled }

}
