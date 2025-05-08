package com.example.GuideExpert

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.GuideExpert.presentation.MainScreen
import com.example.GuideExpert.ui.theme.GuideExpertTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuideExpertTheme {
                val context = LocalContext.current
                val isLight = rememberSaveable{mutableStateOf(true)}
                val onSetLightStatusBar = {isLightTheme:Boolean -> if(isLight.value != isLightTheme) isLight.value = isLightTheme}
                MainScreen(onSetLightStatusBar)
                LaunchedEffect(isLight.value) {
                    setStatusBarColor((context as Activity).window,isLight)
                }
            }
        }
    }
}

fun setStatusBarColor(window: Window, isLight: MutableState<Boolean>) {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.isAppearanceLightStatusBars = isLight.value
}
