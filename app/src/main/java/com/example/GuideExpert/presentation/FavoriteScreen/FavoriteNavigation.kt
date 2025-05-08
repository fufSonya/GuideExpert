package com.example.GuideExpert.presentation.FavoriteScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumExcursion
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen.AlbumExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen.ImageExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.DetailScreen.ExcursionDetailScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.ExcursionDetail
import com.example.GuideExpert.presentation.ExcursionsScreen.ImageExcursion
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToAlbum
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToExcursionDetail
import com.example.GuideExpert.presentation.ExcursionsScreen.navigateToImage
import com.example.GuideExpert.presentation.FavoriteScreen.FavoriteMainScreen.Favorites
import kotlinx.serialization.Serializable

@Serializable
object FavoriteList

@Composable
fun NavigationFavoriteScreen(snackbarHostState: SnackbarHostState,
                             onChangeVisibleBottomBar: (Boolean) -> Unit,
                             innerPadding: PaddingValues,
                             onSetLightStatusBar: (Boolean) -> Unit,
                             onNavigateToProfile: () -> Unit,
)  {
    val navController = rememberNavController()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(navController = navController, startDestination = FavoriteList) {
        val onNavigateToProfileInfo = {onNavigateToProfile() }
        val onNavigateToExcursion = { it: Excursion -> navController.navigateToExcursionDetail(excursion = it) }
        val onNavigateToAlbum = { excursionId: Int -> navController.navigateToAlbum(excursionId = excursionId) }
        val onNavigateToImage = { imageId: Int, excursionImages: List<Image>, indexImage:Int -> navController.navigateToImage(imageId = imageId,excursionImages=excursionImages,
            indexImage=indexImage) }
        val onNavigateToBack = {navController.popBackStack() }
        favoriteDestination(snackbarHostState,viewModelStoreOwner,onChangeVisibleBottomBar,onNavigateToExcursion,onNavigateToAlbum,onNavigateToImage,onNavigateToBack,innerPadding,onSetLightStatusBar,onNavigateToProfileInfo)
    }
}

fun NavGraphBuilder.favoriteDestination(
    snackbarHostState: SnackbarHostState,
    viewModelStoreOwner: ViewModelStoreOwner,
    onChangeVisibleBottomBar: (Boolean) -> Unit,
    onNavigateToExcursion: (Excursion) -> Unit,
    onNavigateToAlbum: (Int) -> Unit,
    onNavigateToImage: (Int, List<Image>, Int) -> Unit,
    onNavigateToBack: () -> Boolean,
    innerPadding: PaddingValues,
    onSetLightStatusBar: (Boolean) -> Unit,
    onNavigateToProfile: () -> Unit
)
{
    composable<FavoriteList> {
        onChangeVisibleBottomBar(true)
        onSetLightStatusBar(true)
        Favorites(snackbarHostState,onNavigateToExcursion,innerPadding,viewModel = hiltViewModel(viewModelStoreOwner))
    }
    composable<ExcursionDetail>(typeMap = ExcursionDetail.typeMap) {
        onChangeVisibleBottomBar(false)
        onSetLightStatusBar(true)
        ExcursionDetailScreen(onNavigateToAlbum,onNavigateToImage,onNavigateToBack,snackbarHostState,innerPadding,onNavigateToProfile)
    }
    composable<AlbumExcursion> {
        backStackEntry ->
            onChangeVisibleBottomBar(false)
            onSetLightStatusBar(true)
            val excursion = backStackEntry.toRoute<AlbumExcursion>()
            AlbumExcursionScreen(excursionId = excursion.excursionId,navigateToImage=onNavigateToImage)
    }
    composable<ImageExcursion>(typeMap = ImageExcursion.typeMap) {
        backStackEntry ->
            onChangeVisibleBottomBar(false)
            onSetLightStatusBar(false)
            val imageExcursion = backStackEntry.toRoute<ImageExcursion>()
            ImageExcursionScreen(imageExcursion = imageExcursion)
    }
}
