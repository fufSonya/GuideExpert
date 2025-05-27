package com.example.GuideExpert

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.GuideExpert.presentation.MainScreen
import com.example.GuideExpert.presentation.rememberAppState
import com.example.GuideExpert.ui.theme.GuideExpertTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            GuideExpertTheme( isLightStatusBar = appState.isLightStatusBar.value) {
                MainScreen(appState)
            }
        }
    }
}
