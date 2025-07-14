package com.apphico.designsystem.components.checklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.core_model.CheckListItem
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import java.time.LocalDate

@Composable
fun CheckListItemField(
    checkListItem: CheckListItem,
    parentDate: LocalDate?,
    onNameChanged: (String) -> Unit = {},
    onItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val isCheckListItemDone = checkListItem.isDone(parentDate)

    val nameStyle =
        if (isCheckListItemDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough) else MaterialTheme.typography.bodyLarge
    val textColor = MaterialTheme.colorScheme.secondary
    val animatedColor by animateColorAsState(if (!isCheckListItemDone) textColor else textColor.copy(alpha = 0.5f))

    val leadingIcon = if (checkListItem.id != 0L) {
        @Composable {
            Row {
                // TODO Implement list ordering (1.1)
                /*
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icReorder,
                    onClick = {}
                )
                */
                CircleCheckbox(
                    modifier = Modifier
                        // .offset(x = (-8).dp)
                        .align(Alignment.CenterVertically),
                    checked = isCheckListItemDone,
                    onCheckedChanged = {
                        onCheckListItemDoneChanged(checkListItem, it)
                    },
                    tint = animatedColor
                )
            }
        }
    } else null

    SmallTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = checkListItem.name,
        onValueChange = onNameChanged,
        textStyle = nameStyle,
        textColor = animatedColor,
        leadingIcon = leadingIcon,
        trailingIcon = {
            ToDoAppIconButton(
                icon = ToDoAppIcons.icRemove,
                onClick = { onItemRemoved(checkListItem) }
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    )
}

@PreviewLightDark
@Composable
private fun CheckListItemFieldPreview() {
    ToDoAppTheme {
        CheckListItemField(
            checkListItem = CheckListItem(0L, "take dog to a walk", true),
            parentDate = getNowDate(),
            onNameChanged = {},
            onItemRemoved = {},
            onCheckListItemDoneChanged = { _, _ -> }
        )
    }
}