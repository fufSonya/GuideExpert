package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SearchScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SearchResult
import kotlin.math.roundToInt

@Composable
fun MainTopBar(modifier: Modifier = Modifier,
               snackbarHostState: SnackbarHostState,
               navigateToExcursion: (Excursion) -> Unit,
               toolbarHeightDp: Int,
               toolbarOffsetHeightPx: Float,
               scrollingOn:()->Unit,
               scrollingOff:()->Unit,
               onToolbarHeightChange:(Float)->Unit,
               navigateToProfileInfo:()->Unit
){

    var boxVisible by rememberSaveable { mutableStateOf(true) }
    var isLoadSizeTopAppBar by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier =Modifier.height(toolbarHeightDp.dp).offset {
            IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt())
        }.background(color = MaterialTheme.colorScheme.background).fillMaxWidth()
    ){
        Column(modifier = Modifier
            .onGloballyPositioned { coordinates ->
                // Set column height using the LayoutCoordinates
                if (!isLoadSizeTopAppBar) {
                    onToolbarHeightChange(coordinates.size.height.toFloat())
                    isLoadSizeTopAppBar = true
                }
            }
        ) {

            ProfileBox(boxVisible = boxVisible,navigateToProfileInfo = navigateToProfileInfo)

            SearchScreen(
                modifier = Modifier,
                snackbarHostState = snackbarHostState,
                navigateToExcursion = navigateToExcursion,
                scrollingOn = scrollingOn,
                scrollingOff = scrollingOff,
                onActiveChanged = { boxVisible = !it },
                navigateToProfileInfo = navigateToProfileInfo
            ){
                SearchResult()
            }

            HorizontalDivider(thickness=0.2.dp, modifier =  Modifier.shadow(6.dp),color = Color.Gray)
        }
    }
}