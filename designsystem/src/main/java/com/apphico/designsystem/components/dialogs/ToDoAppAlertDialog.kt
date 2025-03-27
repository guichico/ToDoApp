package com.apphico.designsystem.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.components.buttons.AlertButton
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

@Composable
fun ToDoAppAlertDialog(
    title: String,
    message: String,
    dismissButtonText: String,
    onDismissRequest: () -> Unit,
    confirmButtonText: String,
    onConfirmClicked: () -> Unit
) {
    DefaultDialog(
        modifier = Modifier
            .width(304.dp),
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
        onDismissRequest = onDismissRequest
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = ToDoAppTheme.spacing.extraExtraLarge,
                        top = ToDoAppTheme.spacing.extraExtraLarge,
                        end = ToDoAppTheme.spacing.extraExtraLarge
                    )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = ToDoAppTheme.spacing.medium,
                            bottom = ToDoAppTheme.spacing.medium
                        ),
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(
                        bottom = ToDoAppTheme.spacing.medium
                    )
            ) {
                AlertButton(
                    text = dismissButtonText,
                    onClick = onDismissRequest
                )
                AlertButton(
                    text = confirmButtonText,
                    onClick = onConfirmClicked
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AlertDialogPreview() {
    ToDoAppTheme {
        ToDoAppAlertDialog(
            title = "Some title",
            message = "Some message",
            dismissButtonText = "Continue",
            onDismissRequest = {},
            confirmButtonText = "Discard",
            onConfirmClicked = {}
        )
    }
}