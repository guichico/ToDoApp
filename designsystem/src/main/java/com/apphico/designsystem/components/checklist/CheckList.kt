package com.apphico.designsystem.components.checklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.core_model.CheckListItem
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import java.time.LocalDate

@Composable
fun CheckList(
    modifier: Modifier = Modifier,
    checkList: List<CheckListItem>,
    parentDate: LocalDate?,
    textColor: Color,
    onCheckListItemDoneChanged: (CheckListItem, Boolean) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        checkList.forEach { checkListItem ->
            TaskCheckListItem(
                checkListItem = checkListItem,
                parentDate = parentDate,
                textColor = textColor,
                onCheckListItemDoneChanged = onCheckListItemDoneChanged
            )
        }
    }
}

@Composable
private fun TaskCheckListItem(
    checkListItem: CheckListItem,
    parentDate: LocalDate?,
    textColor: Color,
    onCheckListItemDoneChanged: (CheckListItem, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = ToDoAppTheme.spacing.extraSmall)
    ) {
        val isCheckListItemDone = checkListItem.isDone(parentDate)

        val animatedColor by animateColorAsState(if (!isCheckListItemDone) textColor else textColor.copy(alpha = 0.5f))

        val nameStyle = if (isCheckListItemDone) MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
        else MaterialTheme.typography.bodyMedium

        CircleCheckbox(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            checked = isCheckListItemDone,
            onCheckedChanged = { onCheckListItemDoneChanged(checkListItem, it) },
            tint = animatedColor
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = ToDoAppTheme.spacing.extraSmall),
            text = checkListItem.name,
            style = nameStyle,
            color = animatedColor
        )
    }
}

@PreviewLightDark
@Composable
private fun TaskCheckListPreview() {
    ToDoAppTheme {
        CheckList(
            checkList = listOf(
                CheckListItem(
                    name = "matemática",
                    hasDone = true
                ),
                CheckListItem(
                    name = "português"
                )
            ),
            parentDate = getNowDate(),
            textColor = MaterialTheme.colorScheme.primary,
            onCheckListItemDoneChanged = { _, _ -> }
        )
    }
}