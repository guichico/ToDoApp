package com.apphico.designsystem.components.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

@Composable
fun ToDoAppAlertDialog(
    isAlertOpenState: MutableState<Boolean>,
    title: String,
    message: String,
    dismissButtonText: String,
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
        onDismissRequest = { isAlertOpenState.value = false }
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
                    style = MaterialTheme.typography.bodyMedium
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
                    onClick = {
                        isAlertOpenState.value = false
                    }
                )
                AlertButton(
                    text = confirmButtonText,
                    onClick = {
                        isAlertOpenState.value = false
                        onConfirmClicked()
                    }
                )
            }
        }
    }
}

@Composable
private fun AlertButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        onClick = onClick
    ) {
        Text(
            text = text
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AlertDialogPreview(

) {
    ToDoAppTheme {
        ToDoAppAlertDialog(
            isAlertOpenState = remember { mutableStateOf(true) },
            title = "Some title",
            message = "Some message",
            dismissButtonText = "Continuar",
            confirmButtonText = "Descartar",
            onConfirmClicked = {}
        )
    }
}