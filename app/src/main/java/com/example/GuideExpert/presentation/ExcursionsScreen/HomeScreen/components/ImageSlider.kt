package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(modifier: Modifier = Modifier,
                navigateToExcursion: (Excursion) -> Unit,
                viewModel: HomeViewModel = hiltViewModel(),
){

    val configApp = viewModel.configApp.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(initialPage = 0)

    if (configApp.value.banners.isEmpty()) return
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(8600)
            if (pagerState.pageCount > 0) {
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % (pagerState.pageCount)
                )
            }
        }
    }

    Box(Modifier.padding(top=10.dp)) {
        HorizontalPager(
            count = configApp.value.banners.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier
                .height(150.dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .clickable {
                        navigateToExcursion(Excursion(id =configApp.value.banners[page].id,title = "",
                            description = "", images = listOf(), idSort = 0
                        ))
                    }
            ) {
                NetworkImage(
                    contentDescription = "",
                    url = configApp.value.banners[page].photo,//imageSlider[page],
                    width = 350,
                    height = 450
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

}

@Composable
fun NetworkImage(url: String, contentDescription: String?, width: Int, height: Int) {
    SubcomposeAsyncImage(
        model = url/*ImageRequest.Builder(LocalContext.current)
            .data(url)
            .setHeader("User-Agent", "Mozilla/5.0")
            .build()*/,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().height(height.dp),
        loading = {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.requiredSize(48.dp)
            )
        },
        error = {
            Log.d("TAG", url)
            Log.d("TAG", "image load: Error!")
            Log.d("TAG", "something went wrong ${it.result.throwable.localizedMessage}")
        }
    )
}