package com.apphico.designsystem.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    NormalButton(
        modifier = modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        contentPadding = PaddingValues(
            vertical = ToDoAppTheme.spacing.small,
            horizontal = ToDoAppTheme.spacing.medium
        ),
        text = text,
        onClick = onClick
    )
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SmallButtonPreview(
) {
    ToDoAppTheme {
        SmallButton(
            text = "Button",
            onClick = {}
        )
    }
}