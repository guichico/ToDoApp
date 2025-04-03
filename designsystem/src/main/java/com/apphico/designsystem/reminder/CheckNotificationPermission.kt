package com.apphico.designsystem.reminder

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.apphico.designsystem.ComposableLifecycle
import com.apphico.designsystem.R
import com.apphico.designsystem.RequestNotificationPermission
import com.apphico.designsystem.components.dialogs.ToDoAppAlertDialog
import com.apphico.extensions.hasNotificationPermission

@Composable
fun CheckNotificationPermission(
    onShowDialog: () -> Boolean,
    onResult: (Boolean) -> Unit
) {
    val context = LocalContext.current

    var isNotificationPermissionsGranted by rememberSaveable { mutableStateOf(context.hasNotificationPermission()) }
    var isNotificationPermissionsDeniedDialogOpen by rememberSaveable { mutableStateOf(false) }

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (!isNotificationPermissionsGranted && context.hasNotificationPermission()) {
                    isNotificationPermissionsGranted = true
                    isNotificationPermissionsDeniedDialogOpen = false
                }
            }

            else -> {}
        }
    }

    if (onShowDialog()) {
        if (isNotificationPermissionsGranted) {
            onResult(true)
        } else {
            RequestNotificationPermission { isGranted ->
                isNotificationPermissionsGranted = isGranted

                if (!isGranted) {
                    isNotificationPermissionsDeniedDialogOpen = true
                    onResult(false)
                }
            }
        }
    }

    if (isNotificationPermissionsDeniedDialogOpen) {
        ToDoAppAlertDialog(
            title = stringResource(R.string.notification_permission_title),
            message = stringResource(R.string.notification_permission_text),
            dismissButtonText = stringResource(R.string.permission_deny),
            onDismissRequest = { isNotificationPermissionsDeniedDialogOpen = false },
            confirmButtonText = stringResource(R.string.permission_configurations),
            onConfirmClicked = {
                context.startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                )
            }
        )
    }
}