package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.GuideExpert.data.repository.ProfileResources
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.ui.theme.Shadow1
import com.example.GuideExpert.ui.theme.Shadow2
import kotlinx.coroutines.flow.StateFlow

val gradient45 = Brush.linearGradient(
    colors = listOf(Shadow1, Shadow2),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

@Stable
interface ProfileBoxStateScope {
    val profile: StateFlow<Profile?>
    val profileStateFlow: StateFlow<ProfileResources>
    val boxVisible: Boolean
    val navigateToProfileInfo: ()->Unit
}

fun DefaultProfileBoxStateScope(
    profile: StateFlow<Profile?>,
    profileStateFlow: StateFlow<ProfileResources>,
    boxVisible: Boolean,
    navigateToProfileInfo: ()->Unit
): ProfileBoxStateScope {
    return object : ProfileBoxStateScope {
        override val profile: StateFlow<Profile?>
            get() = profile
        override val profileStateFlow: StateFlow<ProfileResources>
            get() = profileStateFlow
        override val boxVisible: Boolean
            get() = boxVisible
        override val navigateToProfileInfo: () -> Unit
            get() = navigateToProfileInfo
    }
}


@Composable
fun rememberDefaultProfileBoxStateScope(
    profile: StateFlow<Profile?>,
    profileStateFlow: StateFlow<ProfileResources>,
    boxVisible: Boolean,
    navigateToProfileInfo: ()->Unit
): ProfileBoxStateScope = remember(profile,profileStateFlow,boxVisible,navigateToProfileInfo) {
    DefaultProfileBoxStateScope(profile,profileStateFlow,boxVisible,navigateToProfileInfo)
}


@Composable
fun ProfileBox(modifier: Modifier = Modifier,
               boxVisible: Boolean,
               navigateToProfileInfo: ()->Unit,
               viewModel: ProfileBoxViewModel = hiltViewModel(),
               scopeState: ProfileBoxStateScope = rememberDefaultProfileBoxStateScope(
                   profile = viewModel.profileFlow,
                   profileStateFlow = viewModel.profileStateFlow,
                   boxVisible = boxVisible,
                   navigateToProfileInfo = navigateToProfileInfo
               )
){
    val profileState = scopeState.profileStateFlow.collectAsStateWithLifecycle()
    when(profileState.value) {
        is ProfileResources.Error -> { scopeState.ProfileBoxContent() }
        is ProfileResources.Loading -> {  scopeState.ProfileBoxLoading()}
        is ProfileResources.Success -> { scopeState.ProfileBoxContent() }
        is ProfileResources.Idle -> { scopeState.ProfileBoxContent() }
    }
}


@Composable
fun ProfileBoxStateScope.ProfileBoxLoading(){
    Box {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.graphicsLayer {
                clip = true
                shape = RoundedCornerShape(50.dp)
            }.clickable { navigateToProfileInfo() }.padding(start = 4.dp, end = 4.dp)){
                Box(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .size(48.dp)
                        .clip(shape= CircleShape)
                        .shimmerEffect()
                )

            }
            Icon( Icons.Filled.Notifications, modifier = Modifier.size(38.dp)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient45, blendMode = BlendMode.SrcAtop)
                    }
                },
                contentDescription = null)
        }
    }
}

@Composable
fun ProfileBoxStateScope.ProfileBoxContent(){
    val profile by profile.collectAsStateWithLifecycle()
    AnimatedVisibility(visible = boxVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1))) {
        Box {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(50.dp)
                }.clickable { navigateToProfileInfo() }.padding(start = 4.dp, end = 4.dp)){
                    if (profile?.avatar == null) {
                        Icon(
                            Icons.Filled.AccountCircle, modifier = Modifier.size(48.dp)
                                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient45, blendMode = BlendMode.SrcAtop)
                                    }
                                },
                            contentDescription = null,tint = Color.Gray
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = profile?.avatar?.url,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(48.dp).clip(CircleShape)
                                .border(1.dp, Color.LightGray, CircleShape),
                            error = {
                                Log.d("TAG", "image load: Error!")
                                Log.d(
                                    "TAG",
                                    "something went wrong ${it.result.throwable.localizedMessage}"
                                )
                            }
                        )

                    }
                }
               /* Icon( Icons.Filled.Notifications, modifier = Modifier.size(38.dp)
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient45, blendMode = BlendMode.SrcAtop)
                        }
                    },
                    contentDescription = null)

                */
            }
        }

    }

}