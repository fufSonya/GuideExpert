package com.example.GuideExpert.presentation.ProfileScreen.EditorProfileScreen

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.ui.theme.Shadow1
import com.example.GuideExpert.ui.theme.Shadow2
import com.example.GuideExpert.utils.isValidBirthday
import com.example.GuideExpert.utils.isValidEmail
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorProfileScreen(snackbarHostState: SnackbarHostState,
                        onNavigateToProfile: () -> Unit,
                        innerPaddingMain: PaddingValues,
                        viewModel: EditorProfileViewModel = hiltViewModel(),
                        scopeState: EditorProfileStateScope = rememberDefaultEditorProfileStateScope(profile = viewModel.profileFlow,
                            viewStateFlow = viewModel.viewStateFlow,
                            handleEvent = viewModel::handleEvent,
                            stateLoadAvatar = viewModel.stateLoadAvatar,
                            effectFlow = viewModel.effectFlow,
                            stateRemoveAvatar = viewModel.stateRemoveAvatar,
                            stateUpdateProfile = viewModel.stateUpdateProfile,
                            snackbarHostState = snackbarHostState,
                            isEditeProfile = viewModel.isEditeProfile
                            ),) {

    val stateLoadAvatar by scopeState.stateLoadAvatar.collectAsStateWithLifecycle()
    val effectFlow by scopeState.effectFlow.collectAsStateWithLifecycle(null)
    val scope = rememberCoroutineScope()

    Box(Modifier.padding(innerPaddingMain).fillMaxSize()) {
         Scaffold(
             topBar = {
                 TopAppBar(
                     title = { Text("") },
                     navigationIcon={ IconButton({ onNavigateToProfile() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back") } },
                     colors = TopAppBarDefaults.topAppBarColors(
                         containerColor = MaterialTheme.colorScheme.background,
                         titleContentColor = MaterialTheme.colorScheme.primary
                     ),
                     modifier = Modifier
                         .height(56.dp)
                         .shadow(6.dp),
                     windowInsets = WindowInsets(0)
                 )
             }
         ) {
                 innerPadding -> scopeState.EditorProfileContent(innerPadding)
         }

        scopeState.ContentRemoveAvatar(effectFlow)

        scopeState.ContentUpdateProfile(effectFlow)

        when(stateLoadAvatar.contentState){
            is LoadAvatarState.Error -> {
                effectFlow?.let {
                    when (it) {
                        is SnackbarEffect.ShowSnackbar -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(it.message)
                            }
                        }
                    }
                }
                scopeState.handleEvent(EditorProfileUiEvent.OnLoadAvatarUIStateSetIdle)
            }
            LoadAvatarState.Idle -> {}
            LoadAvatarState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            LoadAvatarState.Success -> {
                Log.d("LoadAvatarState","Success")
                Toast.makeText(LocalContext.current, stringResource(id = R.string.message_avatar_succes_load), Toast.LENGTH_LONG).show()
                scopeState.handleEvent(EditorProfileUiEvent.OnLoadAvatarUIStateSetIdle)
            }
        }

     }
}

@Composable
fun EditorProfileStateScope.ContentUpdateProfile(effectFlow: SnackbarEffect?) {
    val stateUpdateProfile by stateUpdateProfile.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    when(stateUpdateProfile.contentState){
        is UpdateProfileState.Success -> {
            Toast.makeText(LocalContext.current, stringResource(id = R.string.message_profile_succes_update), Toast.LENGTH_LONG).show()
            handleEvent(EditorProfileUiEvent.OnUpdateProfileUIStateSetIdle)
        }
        is UpdateProfileState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            handleEvent(EditorProfileUiEvent.OnUpdateProfileUIStateSetIdle)
        }
        else -> {}
    }

}

@Composable
fun EditorProfileStateScope.ContentRemoveAvatar(effectFlow: SnackbarEffect?) {
    val stateRemoveAvatar by stateRemoveAvatar.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    when(stateRemoveAvatar.contentState){
        is RemoveAvatarState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            handleEvent(EditorProfileUiEvent.OnRemoveAvatarUIStateSetIdle)
        }
        else -> {}
    }
}

@Stable
interface EditorProfileStateScope {
    val profile: StateFlow<Profile?>
    val viewStateFlow: StateFlow<EditorViewState>
    val handleEvent: (EditorProfileUiEvent) -> Job
    val stateLoadAvatar: StateFlow<LoadAvatarUIState>
    val effectFlow: Flow<SnackbarEffect>
    val stateUpdateProfile: StateFlow<UpdateProfileUIState>
    val stateRemoveAvatar: StateFlow<RemoveAvatarUIState>
    val snackbarHostState: SnackbarHostState
    val isEditeProfile: StateFlow<Boolean>
}


fun DefaultEditorProfileStateScope(
    profile: StateFlow<Profile?>,
    viewStateFlow: StateFlow<EditorViewState>,
    handleEvent : (EditorProfileUiEvent) -> Job,
    stateLoadAvatar: StateFlow<LoadAvatarUIState>,
    effectFlow : Flow<SnackbarEffect>,
    stateUpdateProfile: StateFlow<UpdateProfileUIState>,
    stateRemoveAvatar: StateFlow<RemoveAvatarUIState>,
    snackbarHostState: SnackbarHostState,
    isEditeProfile: StateFlow<Boolean>
): EditorProfileStateScope {
    return object : EditorProfileStateScope {
        override val profile: StateFlow<Profile?>
            get() = profile
        override val viewStateFlow: StateFlow<EditorViewState>
            get() = viewStateFlow
        override val handleEvent: (EditorProfileUiEvent) -> Job
            get() = handleEvent
        override val stateLoadAvatar: StateFlow<LoadAvatarUIState>
            get() = stateLoadAvatar
        override val effectFlow: Flow<SnackbarEffect>
            get() = effectFlow
        override val stateUpdateProfile: StateFlow<UpdateProfileUIState>
            get() = stateUpdateProfile
        override val stateRemoveAvatar: StateFlow<RemoveAvatarUIState>
            get() = stateRemoveAvatar
        override val snackbarHostState:SnackbarHostState
            get() = snackbarHostState
        override val isEditeProfile:StateFlow<Boolean>
            get() = isEditeProfile
    }
}

@Composable
fun rememberDefaultEditorProfileStateScope(
    profile: StateFlow<Profile?>,
    viewStateFlow: StateFlow<EditorViewState>,
    handleEvent: (EditorProfileUiEvent) -> Job,
    stateLoadAvatar: StateFlow<LoadAvatarUIState>,
    effectFlow : Flow<SnackbarEffect>,
    stateUpdateProfile: StateFlow<UpdateProfileUIState>,
    stateRemoveAvatar: StateFlow<RemoveAvatarUIState>,
    snackbarHostState: SnackbarHostState,
    isEditeProfile: StateFlow<Boolean>
): EditorProfileStateScope = remember(profile,viewStateFlow,handleEvent,stateLoadAvatar,effectFlow,stateUpdateProfile,stateRemoveAvatar,snackbarHostState,isEditeProfile) {
    DefaultEditorProfileStateScope(profile,viewStateFlow,handleEvent,stateLoadAvatar,effectFlow,stateUpdateProfile,stateRemoveAvatar,snackbarHostState,isEditeProfile)
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EditorProfileStateScope.EditorProfileContent(innerPadding: PaddingValues, )
{
    val profile by profile.collectAsStateWithLifecycle()
    val isEditeProfile by isEditeProfile.collectAsStateWithLifecycle()

    var firstName by rememberSaveable{mutableStateOf(profile?.firstName)}
    var lastName by rememberSaveable{mutableStateOf(profile?.lastName)}
    var email by rememberSaveable{mutableStateOf(profile?.email)}
    val scrollState = rememberScrollState()
    var isErrorEmail by rememberSaveable { mutableStateOf(false) }

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val currentContext = LocalContext.current

    val selectedDate = rememberSaveable  { mutableStateOf(profile?.birthday?.time) }

   // launches camera permissions
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            handleEvent(EditorProfileUiEvent.OnPermissionGrantedWith(currentContext))
        } else {
            // handle permission denied such as:
            handleEvent(EditorProfileUiEvent.OnPermissionDenied)
        }
    }

    // launches photo picker
    val pickImageFromAlbumLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { url ->
        url?.let { EditorProfileUiEvent.OnFinishPickingImagesWith(currentContext, it) }
            ?.let {
                showBottomSheet = false
                handleEvent(it) }
    }

    Box(modifier = Modifier
        .padding(top = innerPadding.calculateTopPadding())
        .fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
           Column(
                modifier = Modifier
                    .padding(top = 0.dp, start = 15.dp, end = 15.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .weight(1f, false),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {

                Spacer(Modifier.height(10.dp))
                LoadAvatar(onChangeShowBottomSheet = { it: Boolean ->
                    showBottomSheet = it
                })

                Row(
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(profile?.firstName ?: "", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.size(5.dp))
                    Text(profile?.lastName ?: "", fontWeight = FontWeight.Bold)
                }

                Text(
                    stringResource(id = R.string.first_name),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    firstName ?: "",
                    {
                        firstName = it
                        handleEvent(EditorProfileUiEvent.OnFirstNameChanged(it))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Text(
                    stringResource(id = R.string.last_name),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    lastName ?: "",
                    {
                        lastName = it
                        handleEvent(EditorProfileUiEvent.OnLastNameChanged(it))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                SelectSexToModal()

                Text(
                    stringResource(id = R.string.birthday),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )

                DatePickerFieldToModal(selectedDate)

                Text(
                    stringResource(id = R.string.email),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    email ?: "",
                    {
                        isErrorEmail = false
                        email = it
                        handleEvent(EditorProfileUiEvent.OnEmailChanged(it))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    isError = isErrorEmail,
                    trailingIcon = {
                        if (isErrorEmail)
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                if (isErrorEmail) {
                    Text(
                        text = stringResource(id = R.string.error_email),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Text(
                    stringResource(id = R.string.phone),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    profile?.phone ?: "",
                    {},
                    enabled = false,
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Button(
                enabled = isEditeProfile,
                onClick = {
                    isErrorEmail = !email.isValidEmail()
                    if (email.isValidEmail() && selectedDate.value.isValidBirthday()) {
                        handleEvent(EditorProfileUiEvent.OnUpdateProfile)
                    }

                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()

            ) {
                Text(stringResource(id = R.string.save))
            }

        }
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.3f),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier.padding(top = 5.dp, start = 15.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = stringResource(id = R.string.take_photo),
                        modifier = Modifier.graphicsLayer { clip = true
                            shape = RoundedCornerShape(50.dp)
                        }.clickable {  permissionLauncher.launch(Manifest.permission.CAMERA) }
                            .padding(start = 5.dp,end=5.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = stringResource(id = R.string.pick_picture),
                        modifier = Modifier.graphicsLayer {
                            clip = true
                            shape = RoundedCornerShape(50.dp)
                        }.clickable {
                            val mediaRequest =
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            pickImageFromAlbumLauncher.launch(mediaRequest)
                        }.padding(start = 5.dp,end=5.dp)
                    )
                    profile?.avatar?.let {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(text = stringResource(id = R.string.remove_avatar),
                            modifier = Modifier
                                .graphicsLayer {
                                clip = true
                                shape = RoundedCornerShape(50.dp)
                            }.clickable {
                                    showBottomSheet = false
                                    handleEvent(EditorProfileUiEvent.OnDeleteAvatarProfile)}
                                .padding(start = 5.dp,end=5.dp)
                        )
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EditorProfileStateScope.LoadAvatar(onChangeShowBottomSheet:(Boolean) -> Unit) {
    val viewState: EditorViewState by viewStateFlow.collectAsState()
    val profile by profile.collectAsStateWithLifecycle()

    val currentContext = LocalContext.current

    val gradient45 = Brush.linearGradient(
        colors = listOf(Shadow1, Shadow2),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    // launches camera
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            onChangeShowBottomSheet(false)
            handleEvent(EditorProfileUiEvent.OnImageSavedWith(currentContext))
        } else {
            onChangeShowBottomSheet(false)
            // handle image saving error or cancellation
            handleEvent(EditorProfileUiEvent.OnImageSavingCanceled)
        }
    }

    LaunchedEffect(key1 = viewState.tempFileUrl) {
        viewState.tempFileUrl?.let {
            cameraLauncher.launch(it)
        }
    }


    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if (profile?.avatar == null) {
            Icon(
                Icons.Filled.AccountCircle, modifier = Modifier.size(200.dp)
                    .clip(CircleShape)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient45, blendMode = BlendMode.SrcAtop)
                        }
                    }
                    .clickable { onChangeShowBottomSheet(true) },
                contentDescription = null, tint = Color.Gray
            )
        } else {
            SubcomposeAsyncImage(
                model = profile?.avatar?.url,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable { onChangeShowBottomSheet(true) },
                loading = {
                    CircularProgressIndicator(
                        color = Color.Gray,
                        modifier = Modifier.requiredSize(48.dp)
                    )
                },
                error = {
                    Log.d("TAG", "image load: Error!")
                    Log.d(
                        "TAG",
                        "something went wrong ${it.result.throwable.localizedMessage}"
                    )
                }
            )
        }
    }
}


@Composable
fun EditorProfileStateScope.SelectSexToModal(modifier: Modifier = Modifier) {
    val profile by profile.collectAsStateWithLifecycle()

    val sex = rememberSaveable{mutableStateOf(profile?.sex)}
    val male = stringResource(id = R.string.male)
    val female = stringResource(id = R.string.female)

    val sexLocal by remember{derivedStateOf { if (sex.value == "male") male else female }}
    var showModal = rememberSaveable { mutableStateOf(false) }


    Text(
        stringResource(id = R.string.sex),
        color = Color.Gray,
        modifier = Modifier.padding(top = 10.dp)
    )
    OutlinedTextField(
        sexLocal ?: "",
        {  },
        readOnly = true,
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .pointerInput(sexLocal) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal.value = true
                    }
                }
            },
        singleLine = true,
        trailingIcon = {
            Icon(
                Icons.Filled.ArrowDropDown,
                "",
            )
        },
        shape = RoundedCornerShape(12.dp)
    )


    if (showModal.value) {
        CommonDialog(title = stringResource(id = R.string.sex_selected), state = showModal) {
            SingleChoiceView(sex)
        }

    }
}


@Composable
fun EditorProfileStateScope.CommonDialog(
    title: String?,
    state: MutableState<Boolean>,
    content: @Composable (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = {
            state.value = false
        },
        title = title?.let {
            {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = title)
                }
            }
        },
        text = content,
        confirmButton = {
            TextButton(onClick = { state.value = false }) {
                Text("OK")
            }
        }, modifier = Modifier.padding(vertical = 5.dp)
    )
}

@Composable
fun EditorProfileStateScope.SingleChoiceView(sex: MutableState<String?>) {
    val radioOptions = listOf(stringResource(id = R.string.male),
        stringResource(id = R.string.female)
    )
    val index = if (sex.value == "male") 0 else 1
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[index]) }
    Column(
        Modifier.fillMaxWidth()
    ) {
        radioOptions.forEachIndexed { index, text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            sex.value = if (index == 0) "male" else "female"
                            handleEvent(EditorProfileUiEvent.OnSexChanged(sex.value!!))
                            onOptionSelected(text)
                        }
                    )
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        sex.value = if (index == 0) "male" else "female"
                        handleEvent(EditorProfileUiEvent.OnSexChanged(sex.value!!))
                        onOptionSelected(text)
                    }
                )
                Text(
                    text = text
                )
            }
        }
    }
}




@Composable
fun EditorProfileStateScope.DatePickerFieldToModal(selectedDate: MutableState<Long?>) {
   // val profile by profile.collectAsStateWithLifecycle()

  //  var selectedDate by rememberSaveable  { mutableStateOf(profile?.birthday?.time) }
    var isErrorBirthday by rememberSaveable { mutableStateOf(false) }
    var showModal by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        readOnly = true,
        value = selectedDate.let { it.value?.let { it1 -> convertMillisToDate(it1) } } ?: "",
        onValueChange = { },
        placeholder = { Text("DD/MM/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        isError = isErrorBirthday,
    )
    if (isErrorBirthday) {
        Text(
            text = stringResource(id = R.string.error_birthday),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate.value = it
                showModal = false
                it?.let{
                    handleEvent(EditorProfileUiEvent.OnBirthdayChanged(Date(it)))
                }

                isErrorBirthday = !selectedDate.value.isValidBirthday()
                             },
            onDismiss = { showModal = false }
        )
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState  = rememberDatePickerState(
        selectableDates = PastOrPresentSelectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        // val aa = LocalDate.parse("2025-01-11")
        // if (utcTimeMillis == convertLocalDateToTimestampUTC(aa) ) { return false}
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}