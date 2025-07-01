package com.apphico.designsystem.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun NormalButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = textColor
        )
    }
}

@PreviewLightDark
@Composable
private fun NormalButtonPreview() {
    ToDoAppTheme {
        NormalButton(
            text = "Button",
            onClick = {}
        )
    }
}