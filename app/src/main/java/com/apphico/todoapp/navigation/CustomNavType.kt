package com.apphico.todoapp.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class CustomNavType<T : Parcelable>(
    private val clazz: Class<T>,
    private val serializer: KSerializer<T>,
) : NavType<T>(isNullableAllowed = false) {

    override val name: String = clazz.name

    override fun get(bundle: Bundle, key: String): T? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, clazz) as T
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)

    override fun parseValue(value: String): T {
        val decoded = URLDecoder.decode(value, StandardCharsets.UTF_8.toString())
        val json = Json.decodeFromString(serializer, decoded)
        return json
    }

    override fun serializeAsValue(value: T): String {
        val json = Json.encodeToString(serializer, value)
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
        return encoded
    }
}
