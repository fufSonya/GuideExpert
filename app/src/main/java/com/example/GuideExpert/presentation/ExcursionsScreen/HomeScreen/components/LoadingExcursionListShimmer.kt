package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun LoadingExcursionListShimmer(
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        repeat(20) {
            ColumnExcursionShimmer()
        }
    }

}

@Composable
fun ColumnExcursionShimmer(
){
    Column(
        modifier = Modifier.padding(15.dp).fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(shape= RoundedCornerShape(30.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )

    background(
        brush = Brush.horizontalGradient(//linearGradient(
            colors = listOf(
                Color(0xFFDCDCDC),
                Color.White,
                Color(0xFFDCDCDC)
//                Color(0xFFE3CAEE),
//                Color(0xFFBB86FC),
//                Color(0xFFE3CAEE),
            ),
            startX = Offset(startOffsetX, 0f).x,
            endX = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()).x
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}