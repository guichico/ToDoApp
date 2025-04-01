package com.apphico.designsystem.location

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import com.apphico.designsystem.ComposableLifecycle
import com.apphico.designsystem.R
import com.apphico.designsystem.components.dialogs.ToDoAppAlertDialog
import com.apphico.extensions.hasCoarseLocationPermission

@Composable
fun CheckLocationPermission(
    navigateBack: () -> Unit,
    onLocationPermissionGranted: () -> Unit
) {
    val context = LocalContext.current

    var isAnyLocationPermissionAlreadyGranted = rememberSaveable { mutableStateOf(false) }
    var isAllLocationPermissionsDeniedDialogOpen = rememberSaveable { mutableStateOf(false) }

    WhenHasAnyLocationPermission(
        context = context,
        onAnyGranted = {
            isAnyLocationPermissionAlreadyGranted.value = true
            onLocationPermissionGranted()
        },
        onAllDenied = {
            isAllLocationPermissionsDeniedDialogOpen.value = true
        }
    )

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (!isAnyLocationPermissionAlreadyGranted.value && context.hasCoarseLocationPermission()) {
                    isAnyLocationPermissionAlreadyGranted.value = true
                    isAllLocationPermissionsDeniedDialogOpen.value = false

                    onLocationPermissionGranted()
                }
            }

            else -> {}
        }
    }

    ShowAllLocationPermissionsDeniedDialog(
        shouldShow = isAllLocationPermissionsDeniedDialogOpen,
        onDismissRequest = {
            isAllLocationPermissionsDeniedDialogOpen.value = false
            navigateBack()
        },
        onConfirmClicked = {
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:${context.packageName}".toUri())
            )
        }
    )
}

@Composable
private fun ShowAllLocationPermissionsDeniedDialog(
    shouldShow: State<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    if (shouldShow.value) {
        ToDoAppAlertDialog(
            title = stringResource(R.string.location_permission_title),
            message = stringResource(R.string.location_permission_text),
            dismissButtonText = stringResource(R.string.permission_deny),
            onDismissRequest = onDismissRequest,
            confirmButtonText = stringResource(R.string.permission_configurations),
            onConfirmClicked = onConfirmClicked
        )
    }
}