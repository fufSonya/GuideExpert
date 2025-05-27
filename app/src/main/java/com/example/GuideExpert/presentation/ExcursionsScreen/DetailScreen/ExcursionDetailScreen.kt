package com.example.GuideExpert.presentation.ExcursionsScreen.DetailScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.models.Image
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.presentation.ExcursionsScreen.ExcursionDetail
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.DeleteFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.SetFavoriteExcursionUIState
import com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen.components.shimmerEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@Stable
interface ExcursionDetailScope {
    val excursionData: Flow<ExcursionData?>
    val excursionImages: Flow<List<Image>>
    val onNavigateToBack: () -> Unit
    val getFiltersGroups: List<Filter>
    val stateView: StateFlow<UIState>
    val navigateToAlbum: (Int) -> Unit
    val navigateToImage: (Int,List<Image>,Int) -> Unit
    val handleEvent : (ExcursionDetailUiEvent) -> Unit
    val profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>
    val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>
    val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>
    val effectFlow: Flow<SnackbarEffect>
    val snackbarHostState: SnackbarHostState
    val excursionDetail: ExcursionDetail
    val navigateToProfileInfo: () -> Unit
    val navigateToBooking:(Int) -> Unit
    val profile: StateFlow<Profile?>
}

fun DefaultExcursionDetailScope(
    excursionData: Flow<ExcursionData?>,
    excursionImages: Flow<List<Image>>,
    onNavigateToBack: () -> Unit,
    getFiltersGroups: List<Filter>,
    stateView: StateFlow<UIState>,
    navigateToAlbum: (Int) -> Unit,
    navigateToImage: (Int,List<Image>,Int) -> Unit,
    handleEvent : (ExcursionDetailUiEvent) -> Unit,
    profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    excursionDetail: ExcursionDetail,
    navigateToProfileInfo: () -> Unit,
    navigateToBooking:(Int) -> Unit,
    profile: StateFlow<Profile?>
): ExcursionDetailScope {
    return object : ExcursionDetailScope {
        override val excursionData: Flow<ExcursionData?>
            get() = excursionData
        override val excursionImages: Flow<List<Image>>
            get() = excursionImages
        override val onNavigateToBack: () -> Unit
            get() = onNavigateToBack
        override val getFiltersGroups: List<Filter>
            get() = getFiltersGroups
        override val stateView: StateFlow<UIState>
            get() = stateView
        override val navigateToAlbum: (Int) -> Unit
            get() = navigateToAlbum
        override val navigateToImage: (Int,List<Image>,Int) -> Unit
            get() = navigateToImage
        override val handleEvent: (ExcursionDetailUiEvent) -> Unit
            get() = handleEvent
        override val profileFavoriteExcursionIdFlow: StateFlow<List<ExcursionFavorite>>
            get() = profileFavoriteExcursionIdFlow
        override val stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>
            get() = stateSetFavoriteExcursion
        override val stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>
            get() = stateDeleteFavoriteExcursion
        override val effectFlow: Flow<SnackbarEffect>
            get() = effectFlow
        override val snackbarHostState: SnackbarHostState
            get() = snackbarHostState
        override val excursionDetail: ExcursionDetail
            get() = excursionDetail
        override val navigateToProfileInfo: () -> Unit
            get() = navigateToProfileInfo
        override val navigateToBooking: (Int) -> Unit
            get() = navigateToBooking
        override val profile: StateFlow<Profile?>
            get() = profile
    }
}

@Composable
fun rememberDefaultExcursionDetailScope(
    excursionData: Flow<ExcursionData?>,
    excursionImages: Flow<List<Image>>,
    onNavigateToBack: () -> Unit,
    getFiltersGroups: List<Filter>,
    stateView: StateFlow<UIState>,
    navigateToAlbum: (Int) -> Unit,
    navigateToImage: (Int,List<Image>,Int) -> Unit,
    handleEvent : KFunction1<ExcursionDetailUiEvent, Unit>,
    profileFavoriteExcursionIdFlow:  StateFlow<List<ExcursionFavorite>>,
    stateSetFavoriteExcursion: StateFlow<SetFavoriteExcursionUIState>,
    stateDeleteFavoriteExcursion: StateFlow<DeleteFavoriteExcursionUIState>,
    effectFlow: Flow<SnackbarEffect>,
    snackbarHostState: SnackbarHostState,
    excursionDetail: ExcursionDetail,
    navigateToProfileInfo: () -> Unit,
    navigateToBooking:(Int) -> Unit,
    profile: StateFlow<Profile?>
): ExcursionDetailScope = remember(excursionData,excursionImages,onNavigateToBack,getFiltersGroups,stateView,navigateToAlbum,navigateToImage,handleEvent,profileFavoriteExcursionIdFlow,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,effectFlow,snackbarHostState,excursionDetail,navigateToProfileInfo,navigateToBooking,profile) {
    DefaultExcursionDetailScope(excursionData,excursionImages,onNavigateToBack,getFiltersGroups,stateView,navigateToAlbum,navigateToImage,handleEvent,profileFavoriteExcursionIdFlow,stateSetFavoriteExcursion,stateDeleteFavoriteExcursion,effectFlow,snackbarHostState,excursionDetail,navigateToProfileInfo,navigateToBooking,profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcursionDetailScreen(
    navigateToAlbum: (Int) -> Unit,
    navigateToImage: (Int,List<Image>,Int) -> Unit,
    onNavigateToBack:() -> Unit,
    snackbarHostState: SnackbarHostState,
    innerPaddingMain: PaddingValues,
    navigateToProfileInfo: () -> Unit,
    navigateToBooking:(Int) -> Unit,
    viewModel: ExcursionDetailViewModel = hiltViewModel(),
    scopeState:ExcursionDetailScope = rememberDefaultExcursionDetailScope(
        excursionData = viewModel.excursion,
        excursionImages = viewModel.images,
        onNavigateToBack = onNavigateToBack,
        getFiltersGroups = viewModel.getFiltersGroups(),
        stateView = viewModel.stateView,
        navigateToAlbum = navigateToAlbum,
        navigateToImage = navigateToImage,
        handleEvent = viewModel::handleEvent,
        profileFavoriteExcursionIdFlow = viewModel.profileFavoriteExcursionIdFlow,
        stateSetFavoriteExcursion = viewModel.stateSetFavoriteExcursion,
        stateDeleteFavoriteExcursion = viewModel.stateDeleteFavoriteExcursion,
        effectFlow = viewModel.effectFlow,
        snackbarHostState = snackbarHostState,
        excursionDetail = viewModel.excursionDetail,
        navigateToProfileInfo = navigateToProfileInfo,
        navigateToBooking = navigateToBooking,
        profile = viewModel.profileFlow
        ),
  //  dataContent: @Composable ExcursionDetailScope.() -> Unit ={},
) {
    val uiState by scopeState.stateView.collectAsStateWithLifecycle()
    val excursionData by scopeState.excursionData.collectAsStateWithLifecycle(null)
    val favoriteExcursions by scopeState.profileFavoriteExcursionIdFlow.collectAsStateWithLifecycle()
    val effectFlow by scopeState.effectFlow.collectAsStateWithLifecycle(null)
    val profile by scopeState.profile.collectAsStateWithLifecycle(null)


    var isFavorite = false
    excursionData?.let {
        if (favoriteExcursions.any { it1 -> it1.excursionId == it.excursionId }) {isFavorite = true} else {isFavorite = false}
    }

    Scaffold(
        modifier = Modifier.padding(innerPaddingMain),
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon={ IconButton({ scopeState.onNavigateToBack()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")}},
                actions={
                    IconButton({
                        excursionData?.let {
                            if (profile!= null && profile?.id !=0) {
                                if (!isFavorite) {
                                    scopeState.handleEvent(
                                        ExcursionDetailUiEvent.OnSetFavoriteExcursion(
                                            scopeState.excursionDetail.excursion
                                        )
                                    )
                                } else {
                                    scopeState.handleEvent(
                                        ExcursionDetailUiEvent.OnDeleteFavoriteExcursion(
                                            scopeState.excursionDetail.excursion
                                        )
                                    )
                                }
                            } else {
                                navigateToProfileInfo()
                            }
                        }
                    }) { Icon(
                            imageVector =  if (isFavorite) Icons.Filled.Favorite else  Icons.Filled.FavoriteBorder,
                            contentDescription = "featured",
                            tint = if (isFavorite)  Color.Red else MaterialTheme.colorScheme.inverseSurface,
                        )}
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(56.dp).shadow(6.dp),
                windowInsets = WindowInsets(0)
            )
        }
    ) { innerPadding ->
           when(uiState.contentState){
               is ExcursionInfoUIState.Data -> { scopeState.ExcursionDataContent(innerPadding) }
               is ExcursionInfoUIState.Error -> { scopeState.ExcursionDataError(innerPadding) }
               is ExcursionInfoUIState.Idle -> {}
               is ExcursionInfoUIState.Loading -> { LoadingExcursionDetail(innerPadding) }
           }
    }
    scopeState.ContentSetFavoriteContent(effectFlow)
    scopeState.ContentDeleteFavoriteContent(effectFlow)
}

@Composable
fun ExcursionDetailScope.ExcursionDataError(innerPadding: PaddingValues) {
    Column (Modifier.padding(innerPadding).fillMaxSize()){
        Row(
            Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.failedload), color = Color.Gray)
            TextButton({handleEvent(ExcursionDetailUiEvent.OnLoadExcursionInfo) }) {
                Text(stringResource(id = R.string.update), fontSize = 15.sp, color = Color.Blue.copy(alpha =.5f))
            }
        }
        Row {
            ExcursionDataContent(PaddingValues(0.dp))
        }
    }
}


@Composable
fun ExcursionDetailScope.ContentSetFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateSetFavoriteExcursion by stateSetFavoriteExcursion.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateSetFavoriteExcursion.contentState){
        is SetFavoriteExcursionState.Success -> {
            handleEvent(ExcursionDetailUiEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        is SetFavoriteExcursionState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            handleEvent(ExcursionDetailUiEvent.OnSetFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }
}

@Composable
fun ExcursionDetailScope.ContentDeleteFavoriteContent(effectFlow: SnackbarEffect?) {
    val stateDeleteFavoriteExcursion by stateDeleteFavoriteExcursion.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateDeleteFavoriteExcursion.contentState){
        is DeleteFavoriteExcursionState.Success -> {
            handleEvent(ExcursionDetailUiEvent.OnDeleteFavoriteExcursionStateSetIdle)
        }
        is DeleteFavoriteExcursionState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            handleEvent(ExcursionDetailUiEvent.OnDeleteFavoriteExcursionStateSetIdle)
        }
        else -> {}
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcursionDetailScope.ExcursionDataContent(innerPadding: PaddingValues) {
    val excursionData by excursionData.collectAsStateWithLifecycle(null)
    val excursionImages by excursionImages.collectAsStateWithLifecycle(null)
    val profile by profile.collectAsStateWithLifecycle(null)

    val scrollState = rememberScrollState()

    Column(Modifier.padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding()).fillMaxSize()
        .verticalScroll(scrollState)) {
        excursionImages?.let {
            if (it.isNotEmpty()){
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { it.count() },
                    modifier = Modifier.padding(top = 5.dp, start = 5.dp,end = 5.dp).fillMaxWidth().height(250.dp),
                    preferredItemWidth = 350.dp,
                    itemSpacing = 1.dp,
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) { index ->
                    val item = it[index]
                    Card(
                        modifier = Modifier.height(250.dp).maskClip(MaterialTheme.shapes.extraLarge)
                    ) {
                        NetworkImageCarousel(
                            item.url,
                            "",
                            500,
                            250,
                            navigateToImage,
                            item.id,
                            it,
                            index
                        )
                    }
                }
                TextButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { excursionData?.let { navigateToAlbum(it.excursionId) } }) {
                    Text(stringResource(id = R.string.showall), color = Color.Blue)
                }

                excursionData?.let{
                    Column(modifier = Modifier.padding(start = 10.dp, end=10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                        Text(
                            text = it.description,
                            modifier = Modifier.fillMaxHeight(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
                        if (getFiltersGroups.isNotEmpty()) {
                            val group = getFiltersGroups
                                .filter { idGroup -> it.group == idGroup.id }
                                .map { it.description }.first()
                            Text(
                                text = group,
                                color =Color.Gray,
                                modifier = Modifier.height(24.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = it.text,
                            modifier = Modifier.fillMaxHeight(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Button(
                onClick = {

                    if (profile!= null && profile?.id !=0) {
                        excursionData?.let { navigateToBooking(it.excursionId) }
                    } else {
                        navigateToProfileInfo()
                    }


                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()

            ) {
                Text(stringResource(id = R.string.booking))
            }

        }

    }
}

@Composable
fun NetworkImageCarousel(url: String, contentDescription: String?, width: Int, height: Int,
                         navigateToImage: (Int,List<Image>,Int) -> Unit, imageId:Int,
                         excursionImages: List<Image>,
                         indexImage:Int) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().height(height.dp).clickable { navigateToImage(imageId,excursionImages,indexImage) },
        loading = {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.requiredSize(48.dp)
            )
        },
        error = {
            Log.d("TAG", url)
            Log.d("TAG", "image load: Error!")
            Log.d("TAG", "something went wrong ${it.result.throwable.localizedMessage}")
        }
    )
}

@Composable
private fun LoadingExcursionDetail(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding).fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(250.dp)
                .clip(shape= RoundedCornerShape(30.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}