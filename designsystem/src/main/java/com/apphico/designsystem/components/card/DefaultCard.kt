package com.apphico.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.MainContainer
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun DefaultCard(
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val animatedBackgroundColor by animateColorAsState(if (enabled) MainContainer else MainContainer.copy(alpha = 0.5f))
    val animatedElevation by animateDpAsState(if (enabled) 8.dp else 0.dp)

    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = ToDoAppTheme.spacing.small)
            .background(
                color = animatedBackgroundColor,
                shape = shape
            )
            .shadow(
                elevation = animatedElevation,
                ambientColor = animatedBackgroundColor,
                spotColor = animatedBackgroundColor,
                shape = shape,
                clip = true
            ),
        content = content
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DefaultCardPreview() {
    ToDoAppTheme {
        DefaultCard(
            onClick = {},
            content = {}
        )
    }
}