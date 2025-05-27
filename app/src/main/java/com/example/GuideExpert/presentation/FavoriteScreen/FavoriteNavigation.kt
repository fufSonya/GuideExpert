package com.example.GuideExpert.presentation.FavoriteScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.presentation.FavoriteScreen.FavoriteMainScreen.Favorites
import kotlinx.serialization.Serializable


@Serializable object FavoritesRoute

@Serializable data object FavoritesBaseRoute

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) =
    navigate(route = FavoritesRoute, navOptions)

fun NavGraphBuilder.favoritesScreen(
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onChangeVisibleBottomBar: (Boolean) -> Unit,
    onNavigateToExcursion: (Excursion) -> Unit,
) {
    navigation<FavoritesBaseRoute>(startDestination = FavoritesRoute) {
        composable<FavoritesRoute> {
            onChangeVisibleBottomBar(true)
            Favorites(snackbarHostState,onNavigateToExcursion,innerPadding)
        }
    }
}
