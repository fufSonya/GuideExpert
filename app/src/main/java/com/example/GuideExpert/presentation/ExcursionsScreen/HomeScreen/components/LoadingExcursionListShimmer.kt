package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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