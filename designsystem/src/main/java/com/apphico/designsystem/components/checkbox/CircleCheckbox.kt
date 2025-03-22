package com.apphico.designsystem.components.checkbox

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons

@Composable
fun CircleCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    tint: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
) {
    Crossfade(
        modifier = modifier,
        targetState = checked,
        label = ""
    ) { isChecked ->
        val icon = remember {
            derivedStateOf { if (isChecked) ToDoAppIcons.icCheckCircle else ToDoAppIcons.icCircle }
        }

        ToDoAppIcon(
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onCheckedChanged(!isChecked)
                },
            icon = icon.value,
            tint = tint,
            contentDescription = "checkbox"
        )
    }
}