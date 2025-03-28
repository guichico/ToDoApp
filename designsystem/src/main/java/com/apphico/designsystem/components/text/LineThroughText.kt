package com.apphico.designsystem.components.text

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun LineThroughText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    isLineThrough: Boolean
) {
    val textStyle = if (isLineThrough) MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough)
    else MaterialTheme.typography.titleMedium

    val animatedColor by animateColorAsState(if (!isLineThrough) textColor else textColor.copy(alpha = 0.5f))

    Text(
        modifier = modifier,
        text = text,
        style = textStyle,
        color = animatedColor
    )
}

@PreviewLightDark
@Composable
private fun LineThroughTextPreview() {
    ToDoAppTheme {
        LineThroughText(
            text = "Some text",
            isLineThrough = true
        )
    }
}
