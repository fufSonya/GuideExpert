package com.example.GuideExpert.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.GuideExpert.presentation.AppState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeBaseRoute
import com.example.GuideExpert.presentation.ExcursionsScreen.homeScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToAlbum
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToBooking
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToExcursionDetail
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToHome
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToImage
import com.example.GuideExpert.presentation.FavoriteScreen.favoritesScreen
import com.example.GuideExpert.presentation.ProfileScreen.navigateToEditorProfile
import com.example.GuideExpert.presentation.ProfileScreen.navigateToProfile
import com.example.GuideExpert.presentation.ProfileScreen.navigateToYandex
import com.example.GuideExpert.presentation.ProfileScreen.profileScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavHost(
    appState: AppState,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeScreen(
            innerPadding = innerPadding,
            snackbarHostState = snackbarHostState,
            onNavigateToExcursion = navController::navigateToExcursionDetail,
            onNavigateToAlbum =  navController::navigateToAlbum,
            onNavigateToImage = navController::navigateToImage,
            onNavigateToBack = navController::popBackStack,
            onNavigateToProfileInfo = {navController.navigateToProfile(navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            })},
            onChangeVisibleBottomBar = { visible: Boolean -> appState.changeVisibleBottomBar(visible) },
            onSetLightStatusBar = { isLight: Boolean -> appState.setLightStatusBar(isLight) },
            onNavigateToBooking =  navController::navigateToBooking,
        )
        favoritesScreen(
            innerPadding = innerPadding,
            snackbarHostState = snackbarHostState,
            onChangeVisibleBottomBar = { visible: Boolean -> appState.changeVisibleBottomBar(visible) },
            onNavigateToExcursion = navController::navigateToExcursionDetail
        )
        profileScreen(
            innerPadding = innerPadding,
            snackbarHostState = snackbarHostState,
            onChangeVisibleBottomBar = { visible: Boolean ->
                appState.changeVisibleBottomBar(
                    visible
                )
            },
            onNavigateToEditorProfile = navController::navigateToEditorProfile,
            onNavigateToYandex = navController::navigateToYandex,
            onNavigateToBack = {navController.navigateToHome(navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            })},
            onNavigateToProfile = navController::navigateToProfile
        )

    }
}
