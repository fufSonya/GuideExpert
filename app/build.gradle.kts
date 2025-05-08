plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    kotlin("plugin.serialization") version "2.0.21"

    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.example.GuideExpert"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.GuideExpert"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["YANDEX_CLIENT_ID"] = "a2f57012385840da950e5f51e761ece9"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        buildConfig = true
    }

    packaging {
        jniLibs.keepDebugSymbols.add("**/libandroidx.graphics.path.so")
        jniLibs.keepDebugSymbols.add("**/libdatastore_shared_counter.so")
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}


dependencies {

    implementation ("androidx.exifinterface:exifinterface:1.4.1")
    implementation ("androidx.datastore:datastore-preferences:1.1.0")

    implementation ("com.yandex.android:authsdk:3.1.3")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("androidx.compose.animation:animation:1.8.0")

    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.media3.common.ktx)
    val room_version = "2.6.1"

    implementation("androidx.room:room-paging:$room_version")

    val paging_version = "3.3.2"
    implementation("androidx.paging:paging-runtime:$paging_version")
    implementation("androidx.paging:paging-compose:3.3.6")


    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.compose.material3:material3:1.3.2")



    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")


    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")


    val nav_version = "2.8.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("com.google.dagger:hilt-android:2.51.1")
    //kapt("com.google.dagger:hilt-android-compiler:2.51.1")
   // ksp("com.google.dagger:hilt-android-compiler:2.51.1")

    ksp("com.google.dagger:hilt-compiler:2.51.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.compose.material:material:1.8.0")

    implementation(libs.kotlinx.serialization.json)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.savedstate:savedstate-ktx:1.2.1")

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}