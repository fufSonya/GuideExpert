package com.example.GuideExpert.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.GuideExpert.R
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeBaseRoute
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeRoute
import com.example.GuideExpert.presentation.FavoriteScreen.FavoritesRoute
import com.example.GuideExpert.presentation.ProfileScreen.ProfileRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconTextId = R.string.search,
        titleTextId = R.string.search,
        route = HomeRoute::class,
        baseRoute = HomeBaseRoute::class,
    ),
    FAVORITES(
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        iconTextId = R.string.favorites,
        titleTextId = R.string.favorites,
        route = FavoritesRoute::class,
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.person,
        titleTextId = R.string.person,
        route = ProfileRoute::class,
    ),
}