package com.example.GuideExpert.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.GuideExpert.presentation.navigation.AppNavHost
import kotlin.reflect.KClass


private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreen(appState: AppState,viewModel: MainViewModel = hiltViewModel()) {

    val currentDestination = appState.currentDestination
    val snackbarHostState = remember { SnackbarHostState() }

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination
                    .isRouteInHierarchy(destination.baseRoute)
                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected) {
                                destination.selectedIcon
                            } else destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.iconTextId)) },

                    )
            }
        },
        layoutType =  if (appState.bottomBarState.value) NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
            currentWindowAdaptiveInfo()
        ) else NavigationSuiteType.None
    ) {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box{
                AppNavHost(appState = appState,innerPadding,snackbarHostState)
            }
        }
    }
}


