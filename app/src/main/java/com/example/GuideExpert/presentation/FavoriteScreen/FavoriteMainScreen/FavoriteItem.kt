package com.example.GuideExpert.presentation.FavoriteScreen.FavoriteMainScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.NetworkImage
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.scaleEffectClickable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun FavoritesScope.FavoriteItem(
    excursion: Excursion,
    coroutineScope: CoroutineScope,
    animationDuration: Int = 100,
) {
    val currentItem by rememberUpdatedState(excursion)
    var isRemoved by remember { mutableStateOf(false) }
    var stateToMaintain by remember { mutableStateOf<SwipeToDismissBoxValue?>(null) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    isRemoved = true
                    stateToMaintain = it
                }
                else ->{}
            }
            return@rememberSwipeToDismissBoxState false
        },
        positionalThreshold = { it * .35f }
    )


    LaunchedEffect(stateToMaintain) {
        stateToMaintain?.let {
            dismissState.snapTo(it)
            stateToMaintain = null
        }
    }

    val cancelStr = stringResource(id = R.string.cancel)
    val cancelDeletingStr = stringResource(id = R.string.message_deleting_excursion)
    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            coroutineScope.launch() {
                delay(animationDuration.toLong())
                handleEvent(ExcursionsFavoriteUiEvent.OnDeleteFavoriteExcursion(currentItem))
                val result =
                    snackbarHostState.showSnackbar(
                        cancelDeletingStr,
                        cancelStr,
                        true,
                        duration = SnackbarDuration.Short
                    )

                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        handleEvent(ExcursionsFavoriteUiEvent.OnRestoreFavoriteExcursion(currentItem))

                    }
                    SnackbarResult.Dismissed -> {}
                }
                isRemoved = false
            }
        } else {
            dismissState.reset()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = dismissState,
            modifier = Modifier,
            backgroundContent = { DismissBackground(dismissState)},
            content = {
                ExcursionFavoriteCard(excursion,coroutineScope)
            })

    }

}

@Composable
fun FavoritesScope.ExcursionFavoriteCard(
    excursion: Excursion,
    coroutineScope: CoroutineScope
) {
    val cancelStr = stringResource(id = R.string.cancel)
    val cancelDeletingStr = stringResource(id = R.string.message_deleting_excursion)
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(15.dp),
    ){
        Row( modifier = Modifier.clickable{
            navigateToExcursion(excursion)
        }){
            Column ( modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically))
            {
                Box {
                    ExcursionImage(excursion)
                    Box(modifier = Modifier.size(48.dp).scaleEffectClickable(onClick = {
                        coroutineScope.launch() {
                            handleEvent(ExcursionsFavoriteUiEvent.OnDeleteFavoriteExcursion(excursion))

                            val result = snackbarHostState.showSnackbar(cancelDeletingStr, cancelStr, true, duration = SnackbarDuration.Short)

                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    handleEvent(ExcursionsFavoriteUiEvent.OnRestoreFavoriteExcursion(excursion))
                                }

                                SnackbarResult.Dismissed -> {
                                }
                            }

                        }
                    }).align(Alignment.TopEnd)
                    ) {
                        Image(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "featured",
                            colorFilter = ColorFilter.tint(Color.Red),
                            modifier = Modifier.size(48.dp).align(Alignment.Center))
                        Image(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "featured",
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier.size(48.dp).align(Alignment.Center))
                    }
                }
                Text(text = excursion.title, style = typography.headlineSmall,fontWeight= FontWeight.Bold)
                Text(text = excursion.description,  modifier = Modifier.fillMaxHeight(),
                    style = typography.titleMedium)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ExcursionImage(excursion: Excursion) {
    val pagerState = rememberPagerState(initialPage = 0)

    Box(modifier = Modifier
        .heightIn(min = 100.dp, max = 200.dp)
        .fillMaxWidth()
        .graphicsLayer {
            clip = true
            shape = RoundedCornerShape(16.dp)
        }) {
        HorizontalPager(
            count = excursion.images.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier
                .heightIn(min = 100.dp, max = 200.dp)
                .fillMaxWidth()
        ) { page ->
            NetworkImage(
                contentDescription = "",
                url = excursion.images[page].url,
                width = 350,
                height = 450
            )
        }
    }
}
