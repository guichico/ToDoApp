package com.apphico.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun DefaultDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Box(
            modifier = modifier
                .shadow(elevation = 8.dp, shape = CardDefaults.shape)
                .background(MaterialTheme.colorScheme.background)
        ) {
            content.invoke()
        }
    }
}

@PreviewLightDark
@Composable
private fun DefaultDialogPreview(
) {
    ToDoAppTheme {
        DefaultDialog(
            onDismissRequest = {}
        ) {
            Text(
                modifier = Modifier.padding(ToDoAppTheme.spacing.extraExtraLarge),
                text = "Show something"
            )
        }
    }
}