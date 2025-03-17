package com.apphico.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

private fun Context.hasPermission(
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasCoarseLocationPermission(): Boolean = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

fun Context.hasFineLocationPermission(): Boolean = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)