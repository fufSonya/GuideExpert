package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.ExcursionsUiEvent
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.HomeScreenContentState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeScreenContentState.FilterItem(
    excursion: Excursion
) {
    val favoriteExcursions by profileFavoriteExcursionIdFlow.collectAsStateWithLifecycle()
    val profile by profile.collectAsStateWithLifecycle(null)
    if (favoriteExcursions.any { it.excursionId == excursion.id }) {excursion.isFavorite = true} else {excursion.isFavorite = false}

    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
         colors = CardDefaults.cardColors(
             containerColor = Color.Transparent,
         ),
        shape = RoundedCornerShape(15.dp),
    ){
        Row( modifier = Modifier.clickable{ navigateToExcursion(excursion) }){
            Column ( modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically))
            {
                Box {
                    ExcursionImage(excursion)
                    Box(modifier = Modifier.size(48.dp).scaleEffectClickable(onClick = {
                            if (profile!= null && profile?.id !=0) {
                                if (!excursion.isFavorite) {
                                    onEvent(ExcursionsUiEvent.OnSetFavoriteExcursion(excursion))
                                } else {
                                    onEvent(ExcursionsUiEvent.OnDeleteFavoriteExcursion(excursion))
                                }
                            } else {
                                navigateToProfileInfo()
                            }
                        }).align(Alignment.TopEnd)
                    ) {
                        Image(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "featured",
                            colorFilter = if (excursion.isFavorite) {ColorFilter.tint(Color.Red)} else {ColorFilter.tint(Color.Gray.copy(alpha = .3f))},
                            modifier = Modifier.fillMaxSize().align(Alignment.Center))
                        Image(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "featured",
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier.fillMaxSize().align(Alignment.Center))
                    }
                }
                Text(text = excursion.title, style = typography.headlineSmall,fontWeight= FontWeight.Bold)
                Text(text = excursion.description,  modifier = Modifier,
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

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.scaleEffectClickable(
    pressValue: Float = 0.75f,
    enabled: Boolean = true,
    onClick: () -> Unit
) =
    composed {
        val selected = remember { mutableStateOf(false) }
        val scale = animateFloatAsState(targetValue = if (selected.value) pressValue else 1f)
        this
            .scale(scale.value)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (enabled) {
                            selected.value = true
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (enabled) {
                            selected.value = false
                            onClick()
                        }
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        if (enabled) {
                            selected.value = false
                        }
                    }
                }
                true
            }

    }