package com.example.GuideExpert.presentation.ProfileScreen.YandexActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileYandexActivity : ComponentActivity() {
    val viewmodel: ProfileYandexViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sdk = YandexAuthSdk.create(YandexAuthOptions(applicationContext))
        val launcher =
            registerForActivityResult(sdk.contract) { result -> handleResult(result) }
        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
        lifecycleScope.launch {
            viewmodel.isClosed.collectLatest {
                if (it) { finish() }
            }
        }
        setContent {
            Box (contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator()
            }
        }

    }

    private  fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> {
                viewmodel.loginYandex(result.token.value)
            }
            is YandexAuthResult.Failure -> {
                Log.d("YANDEX","Failure :: ${result.exception.localizedMessage}")
                finish()
            }
            YandexAuthResult.Cancelled -> {
                Log.d("YANDEX","Cancelled")
                finish()
            }
        }
    }
}