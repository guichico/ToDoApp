package com.apphico.designsystem

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.time.LocalTime

val emptyLambda: () -> Unit = {}

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

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
fun RequestAnyPermissions(
    permissions: Array<String>,
    onResult: (Boolean) -> Unit
) {
    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.reduce { acc, isGranted -> acc || isGranted }
        onResult(isGranted)
    }
    SideEffect {
        requestPermissionsLauncher.launch(permissions)
    }
}
