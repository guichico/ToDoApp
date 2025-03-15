package com.apphico.designsystem.components.icons

import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.designsystem.theme.BaseToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons

@Composable
fun ToDoAppIconButton(
    modifier: Modifier = Modifier,
    icon: BaseToDoAppIcon,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        ToDoAppIcon(
            icon = icon,
            tint = tint
        )
    }
}

@Preview
@Composable
private fun ToDoAppIconButtonPreview(
) {
    ToDoAppIconButton(
        icon = ToDoAppIcons.icFilter,
        onClick = {}
    )
}