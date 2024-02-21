package com.apphico.designsystem.task

import android.content.res.Configuration
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTask
import com.apphico.designsystem.components.card.DefaultCard
import com.apphico.designsystem.components.checkbox.CircleCheckbox
import com.apphico.designsystem.components.checklist.CheckList
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatShortTime

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    DefaultCard(
        onClick = onClick
    ) {
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            task.group?.color?.let {
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
                    task = task,
                    textColor = MaterialTheme.colorScheme.primary
                )
                DateRow(
                    task = task,
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
private fun Header(
    task: Task,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val nameStyle = if (task.isDone) MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough)
        else MaterialTheme.typography.titleMedium

        Text(
            modifier = Modifier
                .weight(1f),
            text = task.name,
            style = nameStyle,
            color = textColor
        )
        CircleCheckbox(
            modifier = Modifier,
            checked = task.isDone,
            onCheckedChange = { },
            tint = textColor
        )
    }
}

@Composable
private fun DateRow(
    task: Task,
    textColor: Color
) {
    Row {
        if (task.startDate != null || task.endDate != null) {
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                task.startDate?.let { taskDate ->
                    Text(
                        text = taskDate.formatShortTime(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                if (task.startDate != null && task.endDate != null) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = ToDoAppTheme.spacing.extraSmall),
                        text = "-",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                task.endDate?.let { taskDate ->
                    Text(
                        text = taskDate.formatShortTime(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
            }
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
            onClick = {}
        )
    }
}