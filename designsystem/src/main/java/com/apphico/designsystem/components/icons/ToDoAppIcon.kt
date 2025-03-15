package com.apphico.designsystem.components.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.designsystem.theme.BaseToDoAppIcon
import com.apphico.designsystem.theme.BaseToDoAppIcon.DrawableResourceIcon
import com.apphico.designsystem.theme.BaseToDoAppIcon.ImageVectorIcon
import com.apphico.designsystem.theme.ToDoAppIcons

@Composable
fun ToDoAppIcon(
    icon: BaseToDoAppIcon,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is ImageVectorIcon -> {
            Icon(
                modifier = modifier,
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                tint = tint
            )
        }

        is DrawableResourceIcon -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = icon.id),
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }
}

@Preview
@Composable
private fun ToDoAppIconPreview(
) {
    ToDoAppIcon(
        icon = ToDoAppIcons.icFilter
    )
}