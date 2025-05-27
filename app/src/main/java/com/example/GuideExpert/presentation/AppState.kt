package com.example.GuideExpert.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToHome
import com.example.GuideExpert.presentation.FavoriteScreen.navigateToFavorites
import com.example.GuideExpert.presentation.ProfileScreen.navigateToProfile
import com.example.GuideExpert.presentation.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        AppState(
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }
}
@Stable
class AppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {
    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)
            return currentEntry.value?.destination
        }


    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.FAVORITES -> navController.navigateToFavorites(topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
        }
    }

    val bottomBarState = mutableStateOf(true)

    fun changeVisibleBottomBar(visible:Boolean) {
        bottomBarState.value = visible
    }

    val isLightStatusBar = mutableStateOf(true)

    fun setLightStatusBar(isLight: Boolean) {
        isLightStatusBar.value = isLight
    }

}