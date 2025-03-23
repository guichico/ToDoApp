package com.apphico.designsystem.components.textfield

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
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
    navigateToSelectLocation: (Location?) -> Unit
) {
    val context = LocalContext.current
    val iconTint = if (isColorDark(MaterialTheme.colorScheme.primaryContainer.toArgb())) White else Black

    NormalTextField(
        modifier = modifier,
        value = task.value.location?.address ?: "",
        placeholder = stringResource(R.string.add_location),
        onClick = {
            task.value.location?.let {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "geo:0,0?q=${it.address}".toUri()
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            } ?: navigateToSelectLocation(task.value.location)
        },
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
                    icon = ToDoAppIcons.icEdit,
                    tint = MaterialTheme.colorScheme.primary,
                    onClick = {
                        navigateToSelectLocation(task.value.location)
                    }
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
            navigateToSelectLocation = {}
        )
    }
}
