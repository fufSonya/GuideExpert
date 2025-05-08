package com.example.GuideExpert.domain.models

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface FilterType {
    data object Sort: FilterType
    data object Categories: FilterType
    data object Duration: FilterType
    data object Groups: FilterType
}

@Stable
class Filter(
    val id: Int,
    val name: String,
    val type: FilterType,
    enabled: Boolean = false,
    val icon: ImageVector? = null,
    val description: String,
) {
    var enabled = mutableStateOf(enabled)
}
