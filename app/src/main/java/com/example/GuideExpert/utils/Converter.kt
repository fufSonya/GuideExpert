package com.example.GuideExpert.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import okhttp3.internal.UTC
import java.time.LocalDate

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun convertLocalDateToTimestampUTC(localDate: LocalDate): Long {
    val zonedDateTime = localDate.atStartOfDay(UTC.toZoneId())
    val instant = zonedDateTime.toInstant()
    return instant.toEpochMilli()
}
