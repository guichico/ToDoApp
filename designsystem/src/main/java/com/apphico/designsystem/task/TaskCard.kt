package com.apphico.designsystem.task

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTask
import com.apphico.designsystem.components.card.MainCard
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.components.checklist.CheckList
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.text.LineThroughText
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatDayAndMonth
import com.apphico.extensions.formatShortTime

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onClick: () -> Unit,
    onDoneCheckedChanged: (Boolean) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Boolean) -> Unit
) {
    MainCard(
        modifier = modifier,
        isDone = task.isDone(),
        group = task.group,
        onClick = onClick
    ) {
        Header(
            task = task,
            onDoneCheckedChanged = onDoneCheckedChanged
        )
        DateRow(
            task = task,
            textColor = MaterialTheme.colorScheme.secondary
        )
        if (task.checkList.isNotEmpty()) {
            CheckList(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-2).dp)
                    .padding(top = ToDoAppTheme.spacing.extraSmall),
                checkList = task.checkList,
                parentDate = task.startDate,
                textColor = MaterialTheme.colorScheme.secondary,
                onCheckListItemDoneChanged = onCheckListItemDoneChanged
            )
        }
    }
}

@Composable
private fun Header(
    task: Task,
    onDoneCheckedChanged: (Boolean) -> Unit
) {
    val isTaskDone = task.isDone()
    val animatedColor by animateColorAsState(
        if (!isTaskDone) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LineThroughText(
            modifier = Modifier
                .weight(1f),
            text = task.name,
            isLineThrough = isTaskDone
        )
        CircleCheckbox(
            modifier = Modifier,
            checked = isTaskDone,
            onCheckedChanged = onDoneCheckedChanged,
            tint = animatedColor
        )
    }
}

@Composable
private fun DateRow(
    task: Task,
    textColor: Color
) {
    val animatedColor by animateColorAsState(if (!task.isDone()) textColor else textColor.copy(alpha = 0.5f))

    Row {
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            if (task.daysOfWeek.isEmpty() && task.startDate == null && task.endDate != null) {
                Text(
                    text = task.endDate!!.formatDayAndMonth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = animatedColor
                )
                task.endTime?.let {
                    Text(
                        text = " às ${it.formatShortTime()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = animatedColor
                    )
                }
            } else if (task.startTime != null || task.endTime != null) {
                task.startTime?.let { startTime ->
                    Text(
                        text = startTime.formatShortTime(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = animatedColor
                    )
                }
                if (task.startTime != null && task.endTime != null) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = ToDoAppTheme.spacing.extraSmall),
                        text = "-",
                        style = MaterialTheme.typography.bodyMedium,
                        color = animatedColor
                    )
                }
                task.endTime?.let { endTime ->
                    Text(
                        text = endTime.formatShortTime(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = animatedColor
                    )
                }
            }
        }
        task.reminder?.let { reminder ->
            Row(
                modifier = Modifier
                    .align(Alignment.Bottom)
            ) {
                ToDoAppIcon(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.CenterVertically),
                    icon = ToDoAppIcons.icReminder,
                    contentDescription = "reminder",
                    tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                )
                Text(
                    modifier = Modifier
                        .padding(start = ToDoAppTheme.spacing.extraSmall),
                    text = task.reminderDateTime()?.formatShortTime()!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                )
            }
        }
    }
}

class TaskCardPreviewProvider : PreviewParameterProvider<Task> {
    override val values = sequenceOf(mockedTask)
}

@PreviewLightDark
@Composable
private fun TaskCardPreview(
    @PreviewParameter(TaskCardPreviewProvider::class) task: Task
) {
    ToDoAppTheme {
        TaskCard(
            task = task,
            onClick = {},
            onDoneCheckedChanged = {},
            onCheckListItemDoneChanged = { _, _ -> }
        )
    }
}