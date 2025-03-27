package com.apphico.designsystem.components.dialogs

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.R
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun showDiscardChangesDialogOnBackIfNeed(
    hasChanges: () -> Boolean,
    navigateBack: () -> Unit
): () -> Unit {
    var isDiscardChangesDialogOpen by remember { mutableStateOf(false) }

    val backConfirm = {
        if (hasChanges()) {
            isDiscardChangesDialogOpen = true
        } else {
            navigateBack()
        }
    }

    if (isDiscardChangesDialogOpen) {
        DiscardChangesDialog(
            onContinueClicked = { isDiscardChangesDialogOpen = false },
            onDiscardClicked = {
                isDiscardChangesDialogOpen = false
                navigateBack()
            }
        )
    }

    BackHandler { backConfirm() }

    return backConfirm
}

@Composable
private fun DiscardChangesDialog(
    onContinueClicked: () -> Unit,
    onDiscardClicked: () -> Unit
) {
    ToDoAppAlertDialog(
        title = stringResource(R.string.discard_changes_title),
        message = stringResource(R.string.discard_changes_message),
        dismissButtonText = stringResource(R.string.continue_btn),
        onDismissRequest = onContinueClicked,
        confirmButtonText = stringResource(R.string.discard),
        onConfirmClicked = onDiscardClicked
    )
}

@PreviewLightDark
@Composable
private fun DiscardChangesDialogPreview(
) {
    ToDoAppTheme {
        DiscardChangesDialog(
            onContinueClicked = {},
            onDiscardClicked = {}
        )
    }
}