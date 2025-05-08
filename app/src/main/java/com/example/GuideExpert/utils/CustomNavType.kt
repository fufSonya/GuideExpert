package com.example.GuideExpert.utils

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavType
import com.example.GuideExpert.domain.models.Image
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) : T? {
        return bundle.getString(key)?.let<String, T>(json::decodeFromString)
    }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))//json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putSerializable(key, json.encodeToString(value) as java.io.Serializable)
    }

}

inline fun <reified T : Any> serializableTypeArray(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<List<T>>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): List<T>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelableArrayList(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelableArrayList(key)
        }
    }

    override fun parseValue(value: String): List<T> {
        return json.decodeFromString(value)
    }

    override fun serializeAsValue(value: List<T>): String {
        return Uri.encode(json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: List<T>) {
        bundle.putSerializable(key, value as java.io.Serializable)
    }
}


