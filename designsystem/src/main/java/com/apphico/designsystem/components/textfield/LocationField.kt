package com.apphico.designsystem.components.textfield

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.net.toUri
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.designsystem.R
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun LocationField(
    modifier: Modifier = Modifier,
    task: State<Task>,
    navigateToSelectLocation: (Location?) -> Unit
) {
    val context = LocalContext.current

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
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            task.value.location?.let {
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icEdit,
                    tint = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        navigateToSelectLocation(task.value.location)
                    }
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun LocationFieldPreview() {
    ToDoAppTheme {
        LocationField(
            task = remember { mutableStateOf(Task()) },
            navigateToSelectLocation = {}
        )
    }
}
