package com.apphico.designsystem.components.switch

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White

@Composable
fun ToDoAppSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = White,
            checkedTrackColor = MaterialTheme.colorScheme.secondary,
            checkedBorderColor = MaterialTheme.colorScheme.secondary,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = White,
            uncheckedBorderColor = MaterialTheme.colorScheme.secondary,
        )
    )
}

class ToDoAppSwitchPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@PreviewLightDark
@Composable
fun ToDoAppSwitchPreview(
    @PreviewParameter(ToDoAppSwitchPreviewProvider::class) isChecked: Boolean
) {
    ToDoAppTheme {
        ToDoAppSwitch(
            checked = isChecked,
            onCheckedChange = {}
        )
    }
}