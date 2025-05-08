package com.example.GuideExpert.presentation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.GuideExpert.R
import com.example.GuideExpert.presentation.ExcursionsScreen.ExcursionsScreen
import com.example.GuideExpert.presentation.FavoriteScreen.FavoriteScreen
import com.example.GuideExpert.presentation.ProfileScreen.ProfileScreen
import com.example.GuideExpert.ui.theme.Purple80
import kotlinx.serialization.Serializable


@Serializable
object Home

@Serializable
object Profile

@Serializable
object Favorite

data class TopLevelRoute<T : Any>(
    val title: String,
    val route: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector)

val Context.topLevelRoutes get() =  listOf(
    TopLevelRoute( getString(R.string.search), Home, Icons.Filled.Search,Icons.Outlined.Search),
    TopLevelRoute(getString(R.string.favorites), Favorite, Icons.Filled.Favorite,Icons.Outlined.FavoriteBorder),
    TopLevelRoute(getString(R.string.person), Profile, Icons.Filled.Person,Icons.Outlined.Person)
)

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreen(onSetLightStatusBar: (Boolean) -> Unit,viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var bottomBarState by rememberSaveable { (mutableStateOf(true)) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomBar(navController,bottomBarState)
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Home) {
            composable<Home> {
                ExcursionsScreen(snackbarHostState = snackbarHostState,
                    onChangeVisibleBottomBar = {visibleBottomBar:Boolean -> bottomBarState = visibleBottomBar},
                    onNavigateToProfile = {
                        navController.navigate(Profile){
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    innerPadding,
                    onSetLightStatusBar
                )
            }
            composable<Profile> {
                ProfileScreen(snackbarHostState = snackbarHostState,
                    onChangeVisibleBottomBar = {visibleBottomBar:Boolean -> bottomBarState = visibleBottomBar},
                    onNavigateToHome = {
                        navController.graph.findStartDestination().route?.let { it1 ->
                            navController.navigate(
                                it1
                            ){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    innerPadding)
            }
            composable<Favorite> {
                FavoriteScreen(snackbarHostState = snackbarHostState,
                    onChangeVisibleBottomBar = {visibleBottomBar:Boolean -> bottomBarState = visibleBottomBar},
                    innerPadding,
                    onSetLightStatusBar,
                    onNavigateToProfile = {
                        navController.navigate(Profile){
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController, bottomBarState: Boolean) {
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(100)),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(100)),
        content = {
            BottomNavigation(backgroundColor = Purple80){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                LocalContext.current.topLevelRoutes.forEach { topLevelRoute ->
                    val selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true
                    NavigationBarItem(
                        selected = selected,
                        label = { Text(topLevelRoute.title,
                            color = Color.Black
                        ) },

                        icon = {Icon(
                            imageVector = if (selected) {
                                topLevelRoute.selectedIcon
                            } else topLevelRoute.unselectedIcon,
                            contentDescription = topLevelRoute.title,
                            tint = if (selected) {MaterialTheme.colorScheme.inverseSurface} else Color.Black
                        )},
                        alwaysShowLabel = false,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    )
}
