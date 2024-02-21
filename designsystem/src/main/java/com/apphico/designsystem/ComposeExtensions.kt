package com.apphico.designsystem

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import java.time.LocalTime

val emptyLambda: () -> Unit = {}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.getLocalTime(): LocalTime = LocalTime.of(hour, minute)

fun Modifier.animatedElevation(
    conditionState: State<Boolean>,
    shadowElevation: Float
): Modifier = composed {
    val elevation = remember {
        derivedStateOf { if (conditionState.value) shadowElevation else 0f }
    }

    val elevationAnimation = animateFloatAsState(
        targetValue = elevation.value,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        ),
        label = ""
    )

    graphicsLayer {
        this.shadowElevation = elevationAnimation.value
    }
}

@Composable
fun CheckPermissions(
    permissions: List<String>,
    onPermissionGrantedChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val isPermissionsGranted = permissions
        .map {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        .reduce { acc, isGranted ->
            acc && isGranted
        }

    when {
        isPermissionsGranted -> {
            onPermissionGrantedChanged(true)
        }

        // TODO Implement rationale
        else -> {
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = {
                    val wasPermissionsGranted = it.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }

                    onPermissionGrantedChanged(wasPermissionsGranted)
                }
            )

            SideEffect {
                permissionLauncher.launch(permissions.toTypedArray())
            }
        }
    }
}