package com.apphico.designsystem.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroup
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.theme.ToDoAppIcons

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
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icRemove,
                    tint = MaterialTheme.colorScheme.primary,
                    onClick = onGroupRemoved
                )
            }
        } ?: run {
            {
                ToDoAppIcon(
                    icon = ToDoAppIcons.icArrowRight,
                    contentDescription = "arrow",
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                )
            }
        }
    )
}

class GroupFieldPreviewProvider : PreviewParameterProvider<Group> {
    override val values = sequenceOf(mockedGroup)
}

@PreviewLightDark
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