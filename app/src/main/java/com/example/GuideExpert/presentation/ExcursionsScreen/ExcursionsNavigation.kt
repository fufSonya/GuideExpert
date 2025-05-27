package com.example.GuideExpert.presentation.ExcursionsScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen.AlbumExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.AlbumScreen.ImageExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.BookingScreen.BookingExcursionScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.DetailScreen.ExcursionDetailScreen
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.HomeScreen
import com.example.GuideExpert.utils.serializableType
import com.example.GuideExpert.utils.serializableTypeArray
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable data object HomeRoute

@Serializable data object HomeBaseRoute

@Serializable
data class AlbumExcursion(
    val excursionId:Int
)

@Serializable
data class BookingExcursion(
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

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)


fun NavGraphBuilder.homeScreen(
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onNavigateToExcursion: (Excursion) -> Unit,
    onNavigateToAlbum: (Int) -> Unit,
    onNavigateToImage: (Int,List<Image>,Int) -> Unit,
    onNavigateToBack:() -> Unit,
    onNavigateToProfileInfo: () -> Unit,
    onChangeVisibleBottomBar: (Boolean) -> Unit,
    onSetLightStatusBar: (Boolean) -> Unit,
    onNavigateToBooking: (Int) -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            onChangeVisibleBottomBar(true)
            onSetLightStatusBar(true)
            HomeScreen(snackbarHostState = snackbarHostState,navigateToExcursion = onNavigateToExcursion,navigateToProfileInfo = onNavigateToProfileInfo,innerPadding = innerPadding)
        }

        composable<ExcursionDetail>(typeMap = ExcursionDetail.typeMap) {
            onChangeVisibleBottomBar(false)
            onSetLightStatusBar(true)
            ExcursionDetailScreen(onNavigateToAlbum,onNavigateToImage,onNavigateToBack,snackbarHostState,innerPadding,onNavigateToProfileInfo,onNavigateToBooking)
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

        composable<BookingExcursion> {
                backStackEntry ->
            onChangeVisibleBottomBar(false)
            onSetLightStatusBar(true)
            val excursion = backStackEntry.toRoute<BookingExcursion>()
            BookingExcursionScreen(excursionId = excursion.excursionId,innerPadding,snackbarHostState = snackbarHostState,navigateToBack = onNavigateToBack )
        }
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

fun NavController.navigateToBooking(excursionId: Int) {
    navigate(route = BookingExcursion(excursionId)){
        launchSingleTop=true
    }
}

fun NavController.navigateToImage(imageId: Int,excursionImages: List<Image>,indexImage:Int) {
    navigate(route = ImageExcursion(imageId,excursionImages,indexImage)){
        launchSingleTop=true
    }
}
