package com.apphico.designsystem.components.dialogs

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.designsystem.R
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun DiscardChangesDialog(
    isAlertDialogOpen: MutableState<Boolean>,
    hasChanges: State<Boolean>,
    navigateBack: () -> Unit
) {
    BackHandler {
        navigateBackConfirm(
            isAlertDialogOpen = isAlertDialogOpen,
            hasChanges = hasChanges,
            navigateBack = navigateBack
        )
    }

    if (isAlertDialogOpen.value) {
        ToDoAppAlertDialog(
            isAlertOpenState = isAlertDialogOpen,
            title = stringResource(R.string.discard_changes_title),
            message = stringResource(R.string.discard_changes_message),
            dismissButtonText = stringResource(R.string.continue_btn),
            confirmButtonText = stringResource(R.string.discard),
            onConfirmClicked = navigateBack
        )
    }
}

fun navigateBackConfirm(
    isAlertDialogOpen: MutableState<Boolean>,
    hasChanges: State<Boolean>,
    navigateBack: () -> Unit
) {
    if (hasChanges.value) {
        isAlertDialogOpen.value = true
    } else {
        navigateBack()
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DiscardChangesDialogPreview(
) {
    ToDoAppTheme {
        DiscardChangesDialog(
            isAlertDialogOpen = remember { mutableStateOf(true) },
            hasChanges = remember { mutableStateOf(true) },
            navigateBack = {}
        )
    }
}