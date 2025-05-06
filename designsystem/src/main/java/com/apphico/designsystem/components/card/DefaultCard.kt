package com.apphico.designsystem.components.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun DefaultCard(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val animatedBackgroundColor by animateColorAsState(
        if (enabled) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    )
    val animatedElevation by animateDpAsState(if (enabled) 8.dp else 0.dp)

    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ToDoAppTheme.spacing.extraSmall)
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
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(vertical = ToDoAppTheme.spacing.extraSmall)
        ) {
            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun DefaultCardPreview() {
    ToDoAppTheme {
        DefaultCard(
            onClick = {},
            content = {}
        )
    }
}