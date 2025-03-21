package com.apphico.designsystem.task

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Group
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTask
import com.apphico.designsystem.components.card.DefaultCard
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.components.checklist.CheckList
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatDayAndMonth
import com.apphico.extensions.formatShortTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun TaskCard(
    task: Task,
    isTaskDone: (Task) -> Flow<Boolean>,
    onClick: () -> Unit,
    onDoneCheckedChange: (Boolean) -> Unit
) {
    val isTaskDone by isTaskDone(task).collectAsState(false)

    DefaultCard(
        enabled = !isTaskDone,
        onClick = onClick
    ) {
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            GroupIndicator(
                group = task.group
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .padding(
                        top = ToDoAppTheme.spacing.small,
                        end = ToDoAppTheme.spacing.medium,
                        bottom = ToDoAppTheme.spacing.small
                    )
            ) {
                Header(
                    taskName = task.name,
                    isTaskDone = isTaskDone,
                    textColor = MaterialTheme.colorScheme.primary,
                    onDoneCheckedChange = onDoneCheckedChange
                )
                DateRow(
                    task = task,
                    isTaskDone = isTaskDone,
                    textColor = MaterialTheme.colorScheme.primary
                )
                if (task.checkList.isNotEmpty()) {
                    CheckList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = (-2).dp)
                            .padding(top = ToDoAppTheme.spacing.extraSmall),
                        checkList = task.checkList,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupIndicator(
    group: Group?
) {
    group?.color?.let {
        Box(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.small),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(color = Color(it), shape = CircleShape)
            )
        }
    } ?: Spacer(modifier = Modifier.width(ToDoAppTheme.spacing.medium))
}

@Composable
private fun Header(
    taskName: String,
    isTaskDone: Boolean,
    textColor: Color,
    onDoneCheckedChange: (Boolean) -> Unit
) {
    val animatedColor by animateColorAsState(if (!isTaskDone) textColor else textColor.copy(alpha = 0.5f))

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val nameStyle = if (isTaskDone) MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough)
        else MaterialTheme.typography.titleMedium

        Text(
            modifier = Modifier
                .weight(1f),
            text = taskName,
            style = nameStyle,
            color = animatedColor
        )
        CircleCheckbox(
            modifier = Modifier,
            checked = isTaskDone,
            onCheckedChange = onDoneCheckedChange,
            tint = animatedColor
        )
    }
}

@Composable
private fun DateRow(
    task: Task,
    isTaskDone: Boolean,
    textColor: Color
) {
    val animatedColor by animateColorAsState(if (!isTaskDone) textColor else textColor.copy(alpha = 0.5f))

    Row {
        if (task.startTime != null || task.endTime != null) {
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
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
        if (task.daysOfWeek.isEmpty() && task.startDate == null && task.endDate != null) {
            Text(
                text = task.endDate!!.formatDayAndMonth(),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
        task.reminder?.let { reminderDate ->
            Row {
                ToDoAppIcon(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.CenterVertically),
                    icon = ToDoAppIcons.icReminder,
                    contentDescription = "reminder",
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
                Text(
                    modifier = Modifier
                        .padding(start = ToDoAppTheme.spacing.extraSmall),
                    text = reminderDate.formatShortTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            }
        }
    }
}

class TaskCardPreviewProvider : PreviewParameterProvider<Task> {
    override val values = sequenceOf(mockedTask)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TaskCardPreview(
    @PreviewParameter(TaskCardPreviewProvider::class) task: Task
) {
    ToDoAppTheme {
        TaskCard(
            task = task,
            isTaskDone = { flow { false } },
            onClick = {},
            onDoneCheckedChange = {}
        )
    }
}