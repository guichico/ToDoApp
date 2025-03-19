package com.apphico.designsystem.components.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

@Composable
fun AlertButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = onClick
    ) {
        Text(
            text = text,
            color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
        )
    }
}

@Preview
@Composable
fun AlertButtonPreview() {
    ToDoAppTheme {
        AlertButton(
            text = "Save",
            onClick = {}
        )
    }
}