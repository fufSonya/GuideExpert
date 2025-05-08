package com.example.GuideExpert.presentation.ProfileScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.GuideExpert.presentation.ProfileScreen.EditorProfileScreen.EditorProfileScreen
import com.example.GuideExpert.presentation.ProfileScreen.ProfileMainScreen.ProfileInfo
import com.example.GuideExpert.presentation.ProfileScreen.YandexActivity.ProfileYandexActivity
import kotlinx.serialization.Serializable

@Serializable
object ProfileInfo

@Serializable
object EditorProfile

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavigationProfileScreen(snackbarHostState: SnackbarHostState,
                            onChangeVisibleBottomBar: (Boolean) -> Unit,
                            onNavigateToHome:()->Unit,
                            innerPadding: PaddingValues
)  {
    val navController = rememberNavController()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(navController = navController, startDestination = ProfileInfo) {
        val onNavigateEditorProfile = { navController.navigateToEditorProfile()}
        val onNavigateToYandex = { navController.navigateToYandex()}
        val onNavigateToBack = { onNavigateToHome() }
        val onNavigateToProfile = {navController.popBackStack()}
        profileDestination(snackbarHostState,onNavigateEditorProfile,onNavigateToYandex,onNavigateToBack, onNavigateToProfile,viewModelStoreOwner,onChangeVisibleBottomBar,innerPadding)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.profileDestination(snackbarHostState :SnackbarHostState,
                                       onNavigateToEditorProfile: () -> Unit,
                                       onNavigateToYandex: () -> Unit,
                                       onNavigateToBack: () -> Unit,
                                       onNavigateToProfile: () -> Boolean,
                                       viewModelStoreOwner: ViewModelStoreOwner,
                                       onChangeVisibleBottomBar: (Boolean) -> Unit,
                                       innerPadding: PaddingValues
)
{
    composable<ProfileInfo> {
        onChangeVisibleBottomBar(true)
        ProfileInfo(snackbarHostState,onNavigateToYandex,onNavigateToEditorProfile, onNavigateToBack,innerPadding,viewModel = hiltViewModel(viewModelStoreOwner)) }
    composable<EditorProfile> {
        onChangeVisibleBottomBar(false)
        EditorProfileScreen(snackbarHostState,onNavigateToProfile,innerPadding) }
    activity("loginYandex") { activityClass = ProfileYandexActivity::class }
}

fun NavController.navigateToEditorProfile() {
    navigate(route = EditorProfile){
        launchSingleTop=true
    }
}

fun NavController.navigateToYandex() {
    navigate(route ="loginYandex"){
        launchSingleTop=true
    }
}

