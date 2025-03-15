package com.apphico.designsystem.components.textfield

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.designsystem.R
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

@Composable
fun LocationField(
    modifier: Modifier = Modifier,
    task: State<Task>,
    navigateToSelectLocation: (Location?) -> Unit,
    onLocationRemoved: () -> Unit
) {
    val iconTint = if (isColorDark(MaterialTheme.colorScheme.primaryContainer.toArgb())) White else Black

    NormalTextField(
        modifier = modifier,
        value = task.value.location?.address ?: "",
        placeholder = stringResource(R.string.add_location),
        onClick = { navigateToSelectLocation(task.value.location) },
        leadingIcon = {
            ToDoAppIcon(
                icon = ToDoAppIcons.icLocation,
                contentDescription = "location",
                tint = iconTint
            )
        },
        trailingIcon = {
            task.value.location?.let {
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icRemove,
                    tint = iconTint,
                    onClick = onLocationRemoved
                )
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LocationFieldPreview(
) {
    ToDoAppTheme {
        LocationField(
            task = remember { mutableStateOf(Task()) },
            navigateToSelectLocation = {},
            onLocationRemoved = {}
        )
    }
}
