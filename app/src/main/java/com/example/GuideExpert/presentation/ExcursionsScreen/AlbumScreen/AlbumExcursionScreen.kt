package com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.presentation.ExcursionsScreen.DetailScreen.NetworkImageCarousel
import kotlinx.coroutines.flow.Flow

@Composable
fun AlbumExcursionScreen(
    excursionId: Int,
    navigateToImage: (Int,List<Image>,Int) -> Unit,
    viewModel: AlbumExcursionViewModel = hiltViewModel(),
    images: Flow<List<Image>> = viewModel.getImages(excursionId)
) {

    val excursionImages by images.collectAsStateWithLifecycle(null)
    excursionImages?.let {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.background(Color.Black).fillMaxSize().imePadding()
        ) {
            itemsIndexed(excursionImages!!) { index, item ->
                NetworkImageCarousel(
                    item.url, "", 500, 250, navigateToImage, item.id,
                    excursionImages!!, index
                )
            }
        }

    }
}