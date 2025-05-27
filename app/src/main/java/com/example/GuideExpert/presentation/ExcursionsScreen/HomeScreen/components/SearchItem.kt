package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SearchEvent
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SearchStateScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun SearchStateScope.SearchItem(
    excursion: Excursion,
    onEvent: (SearchEvent) -> Unit,
    navigateToExcursion: (Excursion) -> Unit,
    )
{
    val favoriteExcursions by profileFavoriteExcursionIdFlow.collectAsStateWithLifecycle()
    val profile by profile.collectAsStateWithLifecycle(null)

    if (favoriteExcursions.any { it.excursionId == excursion.id }) {excursion.isFavorite = true} else {excursion.isFavorite = false}

    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Row( modifier = Modifier.clickable{ navigateToExcursion(excursion) }) {
            Column ( modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically))
            {
                Box {
                    ExcursionImageSearch(excursion)
                    Box(modifier = Modifier.size(48.dp).scaleEffectClickable(onClick = {
                        if (profile!= null && profile?.id !=0) {
                            if (!excursion.isFavorite) {
                                onEvent(SearchEvent.OnSetFavoriteExcursion(excursion))
                            } else {
                                onEvent(SearchEvent.OnDeleteFavoriteExcursion(excursion))
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
                Text(text = excursion.title, style = typography.headlineSmall, fontWeight= FontWeight.Bold )
                Text(text = excursion.description, modifier = Modifier.fillMaxHeight(),
                    style = typography.titleMedium)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ExcursionImageSearch(excursion: Excursion) {
    val pagerState = rememberPagerState(initialPage = 0)

    Box(modifier = Modifier.heightIn(min = 100.dp, max = 200.dp)
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
