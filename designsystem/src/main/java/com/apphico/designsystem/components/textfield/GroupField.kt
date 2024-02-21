package com.apphico.designsystem.components.textfield

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

@Composable
fun GroupField(
    modifier: Modifier = Modifier,
    group: State<Group?>,
    addNewGroupPlaceholder: String,
    navigateToSelectGroup: () -> Unit,
    onGroupRemoved: () -> Unit
) {
    NormalTextField(
        modifier = modifier,
        value = group.value?.name ?: "",
        placeholder = addNewGroupPlaceholder,
        onClick = navigateToSelectGroup,
        leadingIcon = group.value?.let {
            {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .aspectRatio(1f)
                        .background(Color(it.color), CircleShape)
                )
            }
        },
        trailingIcon = group.value?.let {
            {
                val iconTint = if (isColorDark(MaterialTheme.colorScheme.primaryContainer.toArgb())) White else Black

                IconButton(
                    onClick = onGroupRemoved
                ) {
                    ToDoAppIcon(
                        icon = ToDoAppIcons.icRemove,
                        contentDescription = "remove group",
                        tint = iconTint
                    )
                }
            }
        } ?: run {
            {
                ToDoAppIcon(
                    icon = ToDoAppIcons.icArrowRight,
                    contentDescription = "arrow",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
        }
    )
}

class GroupFieldPreviewProvider : PreviewParameterProvider<Group> {
    override val values = sequenceOf(mockedGroup)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun GroupFieldPreview(
    @PreviewParameter(GroupFieldPreviewProvider::class) group: Group
) {
    GroupField(
        group = remember { mutableStateOf(group) },
        addNewGroupPlaceholder = "Select group",
        navigateToSelectGroup = {},
        onGroupRemoved = {}
    )
}