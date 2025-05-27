package com.example.GuideExpert.presentation.ProfileScreen.ProfileMainScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.ProfileResources
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.ui.theme.Shadow1
import com.example.GuideExpert.ui.theme.Shadow2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.KSuspendFunction2

@Stable
interface ProfileStateScope {
    val profile: StateFlow<Profile?>
    val profileStateFlow: StateFlow<ProfileResources>
    val effectFlow: Flow<SnackbarEffect>
    val snackbarHostState: SnackbarHostState
    val sendEffectFlow : KSuspendFunction2<String, String?, Unit>
    val navigateToEditorProfile : () -> Unit
    val navigateToYandex : () -> Unit
    val navigateToBack : () -> Unit
    val logout : () -> Unit
}

fun DefaultProfileStateScope(
    profile: StateFlow<Profile?>,
    profileStateFlow: StateFlow<ProfileResources>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    sendEffectFlow: KSuspendFunction2<String, String?, Unit>,
    navigateToEditorProfile : () -> Unit,
    navigateToYandex : () -> Unit,
    navigateToBack : () -> Unit,
    logout : () -> Unit
): ProfileStateScope {
    return object : ProfileStateScope {
        override val profile: StateFlow<Profile?>
            get() = profile
        override val profileStateFlow: StateFlow<ProfileResources>
            get() = profileStateFlow
        override val effectFlow: Flow<SnackbarEffect>
            get() = effectFlow
        override val snackbarHostState: SnackbarHostState
            get() = snackbarHostState
        override val sendEffectFlow: KSuspendFunction2<String, String?, Unit>
            get() = sendEffectFlow
        override val navigateToEditorProfile: () -> Unit
            get() = navigateToEditorProfile
        override val navigateToYandex: () -> Unit
            get() = navigateToYandex
        override val navigateToBack: () -> Unit
            get() = navigateToBack
        override val logout: () -> Unit
            get() = logout
    }
}

@Composable
fun rememberDefaultProfileStateScope(
    profile: StateFlow<Profile?>,
    profileStateFlow: StateFlow<ProfileResources>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    sendEffectFlow: KSuspendFunction2<String, String?, Unit>,
    navigateToEditorProfile : () -> Unit,
    navigateToYandex : () -> Unit,
    navigateToBack : () -> Unit,
    logout: () -> Unit
): ProfileStateScope = remember(profile,profileStateFlow,snackbarHostState,sendEffectFlow,navigateToEditorProfile,navigateToYandex,navigateToBack,logout) {
    DefaultProfileStateScope(profile,profileStateFlow,effectFlow,snackbarHostState,sendEffectFlow,navigateToEditorProfile,navigateToYandex,navigateToBack,logout)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfo(snackbarHostState: SnackbarHostState,
                onNavigateToYandex: () -> Unit,
                onNavigateToEditorProfile: () -> Unit,
                onNavigateToBack: () -> Unit,
                innerPaddingMain: PaddingValues,
                viewModel: ProfileViewModel = hiltViewModel(),
                scopeState: ProfileStateScope = rememberDefaultProfileStateScope(profile = viewModel.profileFlow,
                    profileStateFlow = viewModel.profileStateFlow,
                    effectFlow = viewModel.effectFlow,
                    snackbarHostState = snackbarHostState,
                    sendEffectFlow = viewModel::sendEffectFlow,
                    navigateToYandex = onNavigateToYandex,
                    navigateToEditorProfile = onNavigateToEditorProfile,
                    navigateToBack = onNavigateToBack,
                    logout = viewModel::logout),
) {

    val profileState = scopeState.profileStateFlow.collectAsStateWithLifecycle()

    var expandedDropdownMenu by remember { mutableStateOf(false) }

    val effectFlow by viewModel.effectFlow.collectAsStateWithLifecycle(null)
    LaunchedEffect(effectFlow) {
        effectFlow?.let {
            when (it) {
                is SnackbarEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(innerPaddingMain),
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon={ IconButton({
                    scopeState.navigateToBack()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back") } },
                actions = {
                    if (profileState.value is ProfileResources.Success) {
                        IconButton({
                            expandedDropdownMenu = true
                        }) { Icon(Icons.Filled.MoreVert, contentDescription = "featured") }
                        DropdownMenu(
                            expanded = expandedDropdownMenu,
                            onDismissRequest = { expandedDropdownMenu = false }) {
                            DropdownMenuItem(text = { Text("Редактировать") }, onClick = {
                                expandedDropdownMenu = false
                                scopeState.navigateToEditorProfile()
                            })
                        }
                    }},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(56.dp).shadow(6.dp),
                windowInsets = WindowInsets(0)
            )
        }
    ) { innerPadding ->
        when(profileState.value) {
            is ProfileResources.Error -> {
                Log.d("PROFILESTATE","Error")
                scopeState.ProfileContent(innerPadding)
            }
            is ProfileResources.Loading -> {  Log.d("PROFILESTATE","LOADING")}
            is ProfileResources.Success -> {  Log.d("PROFILESTATE","Success")
                scopeState.ProfileContent(innerPadding)
            }
            is ProfileResources.Idle -> {
                scopeState.ProfileEnter(innerPadding)
            }
        }
    }


}

@Composable
fun ProfileStateScope.ProfileEnter(innerPadding: PaddingValues) {
    Column (modifier = Modifier.padding(innerPadding).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally ){
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.button_yandex),
            contentDescription = "Yandex",
            modifier = Modifier.clickable {
                navigateToYandex()
            }
        )
    }
}

@Composable
fun ProfileStateScope.ProfileContent(innerPadding: PaddingValues) {

    val profile by profile.collectAsStateWithLifecycle()

    val birthday = profile?.birthday?.let { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it) }

    val gradient45 = Brush.linearGradient(
        colors = listOf(Shadow1, Shadow2),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (profile?.avatar == null) {
                Icon(
                    Icons.Filled.AccountCircle, modifier = Modifier.size(240.dp)
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen
                        }
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient45, blendMode = BlendMode.SrcAtop)
                            }
                        }.clip(CircleShape).clickable {  navigateToEditorProfile() },
                    contentDescription = null, tint = Color.Gray
                )
            } else {
                Row(horizontalArrangement = Arrangement.Center) {
                    SubcomposeAsyncImage(
                        model = profile?.avatar?.url,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(200.dp).clip(CircleShape).border(1.dp, Color.LightGray, CircleShape).clickable { navigateToEditorProfile() },
                        loading = {
                            CircularProgressIndicator(
                                color = Color.Gray,
                                modifier = Modifier.requiredSize(48.dp)
                            )
                        },
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

            Row(horizontalArrangement = Arrangement.Center) {
                Text(profile?.firstName ?: "")
                Spacer(Modifier.size(5.dp))
                Text(profile?.lastName ?: "")
            }
            Text(birthday.toString())
            Text(profile?.email ?: "")
            Text(profile?.phone ?: "")
            Spacer(Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.exit),
                modifier = Modifier.clickable { logout() },
                color = Color.Red
                )

        }
    }
}


