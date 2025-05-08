package com.example.GuideExpert.presentation.ExcursionsScreen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen.AlbumExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen.ImageExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.DetailScreen.ExcursionDetailScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.HomeScreen
import com.example.GuideExpert.utils.serializableType
import com.example.GuideExpert.utils.serializableTypeArray
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object ExcursionSearchScreen

@Serializable
data class AlbumExcursion(
    val excursionId:Int
)

@Serializable
data class ImageExcursion(
    val imageId:Int,
    val excursionImages: List<Image>,
    val indexImage:Int
){
    companion object {
        val typeMap = mapOf(typeOf<List<Image>>() to serializableTypeArray<Image>())
     }
}

@Serializable
class ExcursionDetail(val excursion : Excursion) {
    companion object {
        val typeMap = mapOf(typeOf<Excursion>() to serializableType<Excursion>())

        fun from(savedStateHandle: SavedStateHandle) =
            savedStateHandle.toRoute<ExcursionDetail>(typeMap)
    }
}

@Composable
fun NavigationHomeScreen(
    snackbarHostState: SnackbarHostState,
    onChangeVisibleBottomBar: (Boolean) -> Unit,
    onNavigateToProfile: () -> Unit,
    innerPadding: PaddingValues,
    onSetLightStatusBar: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ExcursionSearchScreen) {
        val onNavigateToProfileInfo = {onNavigateToProfile() }
        val onNavigateToExcursion = { it: Excursion -> navController.navigateToExcursionDetail(excursion = it) }
        val onNavigateToAlbum = { excursionId: Int -> navController.navigateToAlbum(excursionId = excursionId) }
        val onNavigateToImage = { imageId: Int,excursionImages: List<Image>,indexImage:Int -> navController.navigateToImage(imageId = imageId,excursionImages=excursionImages,
            indexImage=indexImage) }
        val onNavigateToBack = {navController.popBackStack() }
        excursionsDestination(snackbarHostState,onNavigateToExcursion,onNavigateToAlbum,onNavigateToImage,onChangeVisibleBottomBar,onNavigateToBack,onNavigateToProfileInfo,innerPadding,onSetLightStatusBar)
    }
}

fun NavGraphBuilder.excursionsDestination(
    snackbarHostState: SnackbarHostState,
    onNavigateToExcursion: (Excursion) -> Unit,
    onNavigateToAlbum: (Int) -> Unit,
    onNavigateToImage: (Int, List<Image>, Int) -> Unit,
    onChangeVisibleBottomBar: (Boolean) -> Unit,
    onNavigateToBack: () -> Boolean,
    onNavigateToProfileInfo: () -> Unit,
    innerPadding: PaddingValues,
    onSetLightStatusBar: (Boolean) -> Unit,
) {
    composable<ExcursionSearchScreen> {
        onChangeVisibleBottomBar(true)
        onSetLightStatusBar(true)
        HomeScreen(snackbarHostState,onNavigateToExcursion,onNavigateToProfileInfo,innerPadding)
    }
    composable<ExcursionDetail>(typeMap = ExcursionDetail.typeMap) {
        onChangeVisibleBottomBar(false)
        onSetLightStatusBar(true)
        ExcursionDetailScreen(onNavigateToAlbum,onNavigateToImage,onNavigateToBack,snackbarHostState,innerPadding,onNavigateToProfileInfo)
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

fun NavController.navigateToExcursionDetail(excursion: Excursion) {
    navigate(route = ExcursionDetail(excursion)){
        launchSingleTop=true
    }
}

fun NavController.navigateToAlbum(excursionId: Int) {
    navigate(route = AlbumExcursion(excursionId)){
        launchSingleTop=true
    }
}

fun NavController.navigateToImage(imageId: Int,excursionImages: List<Image>,indexImage:Int) {
    navigate(route = ImageExcursion(imageId,excursionImages,indexImage)){
        launchSingleTop=true
    }
}
