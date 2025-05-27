package com.example.GuideExpert.presentation.ExcursionsScreen.BookingScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.GuideExpert.R
import com.example.GuideExpert.domain.models.SnackbarEffect
import com.example.GuideExpert.utils.isValidEmail
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingExcursionScreen(
    excursionId: Int,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateToBack: () -> Unit,
){

    Box(Modifier.padding(innerPadding).fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        IconButton({
                            navigateToBack()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    },
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
        ) { innerPaddingMain ->
            BookingExcursionContent(excursionId,innerPaddingMain,snackbarHostState,navigateToBack)
        }
    }

}


@Composable
fun BookingExcursionContent(
    excursionId: Int,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateToBack: () -> Unit,
    viewModel: BookingExcursionViewModel = hiltViewModel()
){
    val profile by viewModel.profileFlow.collectAsStateWithLifecycle()
    val selectedDate = rememberSaveable  { mutableStateOf<Long?>(Date().time) }
    viewModel.handleEvent(BookingExcursionUiEvent.OnDateChanged(
        selectedDate.value?.let { convertMillisToDateBooking(it) } ?: ""
    ))

    val selectedTime =  rememberSaveable { mutableStateOf<String>("12:00") }
    viewModel.handleEvent(BookingExcursionUiEvent.OnTimeChanged(selectedTime.value))

    var count by rememberSaveable{mutableStateOf("1")}
    viewModel.handleEvent(BookingExcursionUiEvent.OnCountChanged(count))


    var email by rememberSaveable{mutableStateOf(profile?.email)}
    viewModel.handleEvent(BookingExcursionUiEvent.OnEmailChanged(email ?: ""))
    var isErrorEmail by rememberSaveable { mutableStateOf(false) }

    var phone by rememberSaveable{mutableStateOf(profile?.phone)}
    viewModel.handleEvent(BookingExcursionUiEvent.OnPhoneChanged(phone ?: ""))


    var comments by rememberSaveable{mutableStateOf("")}
    viewModel.handleEvent(BookingExcursionUiEvent.OnCommentsChanged(comments))

    val scrollState = rememberScrollState()

    val stateBookingExcursion by viewModel.stateBookingExcursion.collectAsStateWithLifecycle()
    val effectFlow by viewModel.effectFlow.collectAsStateWithLifecycle(null)
    val scope = rememberCoroutineScope()

    when(stateBookingExcursion.contentState){
        is BookingExcursionState.Success -> {
            Toast.makeText(LocalContext.current, stringResource(id = R.string.message_booking_excursion_succes), Toast.LENGTH_LONG).show()
            viewModel.handleEvent(BookingExcursionUiEvent.OnBookingExcursionUIStateSetIdle)
            navigateToBack()
        }
        is BookingExcursionState.Error -> {
            effectFlow?.let {
                when (it) {
                    is SnackbarEffect.ShowSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            viewModel.handleEvent(BookingExcursionUiEvent.OnBookingExcursionUIStateSetIdle)
        }
        else -> {}
    }


    Box(modifier = Modifier
        .padding(innerPadding)
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    stringResource(id = R.string.select_date_booking),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                DatePickerBookingToModal(selectedDate, viewModel::handleEvent)

                Text(
                    stringResource(id = R.string.select_time_booking),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )

                AdvancedTimePicker(selectedTime, viewModel::handleEvent)

                Text(
                    stringResource(id = R.string.select_count_booking),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )

                OutlinedTextField(
                    count ?: "1",
                    {
                        count = it
                        viewModel.handleEvent(BookingExcursionUiEvent.OnCountChanged(it))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Text(
                    stringResource(id = R.string.select_email_booking),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    email ?: "",
                    {
                        isErrorEmail = false
                        email = it
                        viewModel.handleEvent(BookingExcursionUiEvent.OnEmailChanged(it))
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
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
                    stringResource(id = R.string.select_phone_booking),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    phone ?: "",
                    {
                        phone = it
                        viewModel.handleEvent(BookingExcursionUiEvent.OnPhoneChanged(it))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Text(
                    stringResource(id = R.string.select_comments_booking),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp)
                )
                OutlinedTextField(
                    comments ?: "",
                    {
                        comments = it
                        viewModel.handleEvent(BookingExcursionUiEvent.OnCommentsChanged(it))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )

            }
            Button(
                enabled = selectedDate.value.isValidDate(),
                onClick = {
                    isErrorEmail = !email.isValidEmail()
                    if (email.isValidEmail() && selectedDate.value.isValidDate()) {
                        viewModel.handleEvent(BookingExcursionUiEvent.OnSendRequest(excursionId))
                    }

                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()

            ) {
                Text(stringResource(id = R.string.send_request_booking))
            }
        }
    }
}


@Composable
fun DatePickerBookingToModal(
    selectedDate: MutableState<Long?>,
    handleEvent: (BookingExcursionUiEvent) -> Job
) {
    var showModal by remember { mutableStateOf(false) }
    var isErrorDate by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        readOnly = true,
        value = selectedDate.value?.let { convertMillisToDateBooking(it) } ?: "",
        onValueChange = { },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        isError = isErrorDate,
    )

    if (isErrorDate) {
        Text(
            text = stringResource(id = R.string.error_date_booking),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    if (showModal) {
        DatePickerBookingModal(
            onDateSelected = { selectedDate.value = it
                isErrorDate = !selectedDate.value.isValidDate()

                it?.let{
                    handleEvent(BookingExcursionUiEvent.OnDateChanged(
                        selectedDate.value?.let { it1-> convertMillisToDateBooking(it1) }!!
                    ))
                } },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBookingModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState  = rememberDatePickerState(
        selectableDates = FutureSelectableDates()
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
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDateBooking(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
class FutureSelectableDates: SelectableDates {
    private val now = LocalDate.now()
    private val dayStart = now.atTime(0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000

    @ExperimentalMaterial3Api
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStart
    }

    @ExperimentalMaterial3Api
    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedTimePicker(
    selectedTime: MutableState<String>,
    handleEvent: (BookingExcursionUiEvent) -> Job
) {
    var showDialog by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0,
        is24Hour = true,
    )

    OutlinedTextField(
        readOnly = true,
        value =selectedTime.value,
        onValueChange = {},
        trailingIcon = {
            Icon(Icons.Default.AccessTime, contentDescription = "Select time")
        },
        modifier = Modifier.fillMaxWidth()
            .pointerInput(selectedTime) {
            awaitEachGesture {
                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                // in the Initial pass to observe events before the text field consumes them
                // in the Main pass.
                awaitFirstDown(pass = PointerEventPass.Initial)
                val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                if (upEvent != null) {
                    showDialog = true
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
    )

    if (showDialog) {
        AdvancedTimePickerDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                val minute:String
                if (timePickerState.minute >= 10 ) {
                    minute  = timePickerState.minute.toString()
                } else {
                    minute = "0" + timePickerState.minute.toString()
                }

                val hour:String
                if (timePickerState.hour >= 10 ) {
                    hour  = timePickerState.hour.toString()
                } else {
                    hour = "0" + timePickerState.hour.toString()
                }
                selectedTime.value = hour.plus(":").plus(minute)
                handleEvent(BookingExcursionUiEvent.OnTimeChanged(selectedTime.value)) }
        ) {
            TimeInput(
                state = timePickerState,
            )

        }
    }
}

@Composable
fun AdvancedTimePickerDialog(
    title: String = "Select Time",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier =
                Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface
                    ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}

fun Long?.isValidDate() =  this !== null

