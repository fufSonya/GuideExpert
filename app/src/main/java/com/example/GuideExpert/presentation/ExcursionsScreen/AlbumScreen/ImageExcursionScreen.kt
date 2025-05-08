package com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.NetworkImage
import com.example.GuideExpert.presentation.ExcursionsScreen.ImageExcursion
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageExcursionScreen(
    imageExcursion: ImageExcursion,
) {
    var scale by remember { mutableStateOf(1f) }
    val pagerState = rememberPagerState(initialPage = imageExcursion.indexImage)

    val minScale = 1f
    val maxScale = 3f
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val slowMovement = 0.5f
    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    imageExcursion.excursionImages.let {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.background(Color.Black).fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = scale * zoom
                    scale = newScale.coerceIn(minScale, maxScale)

                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val offsetXChange = (centerX - offsetX) * (newScale / scale - 1)
                    val offsetYChange = (centerY - offsetY) * (newScale / scale - 1)

                    val maxOffsetX = (size.width / 2) * (scale - 1)
                    val minOffsetX = -maxOffsetX
                    val maxOffsetY = (size.height / 2) * (scale - 1)
                    val minOffsetY = -maxOffsetY

                    if (scale * zoom <= maxScale) {
                        offsetX = (offsetX + pan.x * scale * slowMovement + offsetXChange)
                            .coerceIn(minOffsetX, maxOffsetX)
                        offsetY = (offsetY + pan.y * scale * slowMovement + offsetYChange)
                            .coerceIn(minOffsetY, maxOffsetY)
                    }

                    if (pan != Offset(0f, 0f) && initialOffset == Offset(0f, 0f)) {
                        initialOffset = Offset(offsetX, offsetY)
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale != 1f) {
                            scale = 1f
                            offsetX = initialOffset.x
                            offsetY = initialOffset.y
                        } else {
                            scale = 2f
                        }
                    }
                )
            }
        ) {
            HorizontalPager(
                count = imageExcursion.excursionImages.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 0.dp),
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY,
                    )
            ) { page ->
                NetworkImage(
                    contentDescription = "",
                    url = imageExcursion.excursionImages[page].url,
                    width = 350,
                    height = 450
                )

            }
        }
    }
}