package com.example.GuideExpert.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Call
import okhttp3.OkHttpClient

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .respectCacheHeaders(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            .callFactory {
                Call.Factory{
                    val request = it.newBuilder().addHeader("User-Agent", "Mozilla/5.0").build()
                    OkHttpClient().newCall(request)
                }
            }
            .logger(DebugLogger())
            .build()
    }
}