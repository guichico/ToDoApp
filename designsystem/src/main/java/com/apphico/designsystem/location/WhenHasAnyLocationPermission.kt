package com.apphico.designsystem.location

import android.Manifest
import android.content.Context
import androidx.compose.runtime.Composable
import com.apphico.designsystem.RequestAnyPermissions
import com.apphico.extensions.hasCoarseLocationPermission
import com.apphico.extensions.hasFineLocationPermission

@Composable
fun WhenHasAnyLocationPermission(
    context: Context,
    onAnyGranted: () -> Unit,
    onAllDenied: () -> Unit
) {
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    when {
        // Has both permissions
        context.hasCoarseLocationPermission() && context.hasFineLocationPermission() -> {
            onAnyGranted()
            return
        }

        // Has only coarse permission
        context.hasCoarseLocationPermission() && !context.hasFineLocationPermission() -> {
            onAnyGranted() // Can still get an approximate location

            RequestAnyPermissions(
                permissions = locationPermissions.drop(1).toTypedArray()
            ) { isFineGranted ->
                if (isFineGranted) onAnyGranted() // and then get the precise location
            }

            return
        }

        else -> {
            RequestAnyPermissions(
                permissions = locationPermissions
            ) { isAnyGranted ->
                if (isAnyGranted) onAnyGranted() else onAllDenied()
            }
        }
    }
}

