package com.apphico.todoapp.calendar

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTasks
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.task.TaskCard
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatLongDayOfWeekDate
import com.apphico.extensions.formatShortDayOfWeekDate
import com.apphico.extensions.getNowDate
import com.apphico.extensions.isCurrentYear
import com.apphico.extensions.toMillis
import java.time.LocalDate

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel,
    navigateToAddEditTask: (Task?) -> Unit
) {
    val calendar = calendarViewModel.calendar.collectAsState()

    val selectedDate = calendarViewModel.selectedDate.collectAsState()
    val calendarViewMode = calendarViewModel.calendarViewMode.collectAsState()

    CalendarScreenContent(
        selectedDate = selectedDate,
        calendarViewMode = calendarViewMode,
        tasks = calendar,
        navigateToAddEditTask = navigateToAddEditTask,
        onDoneCheckedChanged = { task, isDone -> calendarViewModel.setTaskDone(task, isDone) },
        onCheckListItemDoneChanged = { checkListItem, task, isDone -> calendarViewModel.setCheckListItemDone(checkListItem, task, isDone) }
    )
}

@Composable
private fun CalendarScreenContent(
    selectedDate: State<LocalDate>,
    calendarViewMode: State<CalendarViewMode>,
    tasks: State<List<Task>>,
    navigateToAddEditTask: (Task?) -> Unit,
    onDoneCheckedChanged: (Task, Boolean) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Task, Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(
                start = ToDoAppTheme.spacing.medium,
                top = ToDoAppTheme.spacing.medium,
                end = ToDoAppTheme.spacing.medium,
                bottom = 80.dp
            )
        ) {
            if (calendarViewMode.value == CalendarViewMode.DAY) {
                taskRowsDayViewMode(
                    selectedDate = selectedDate,
                    tasks = tasks.value,
                    onTaskClicked = navigateToAddEditTask,
                    onDoneCheckedChanged = onDoneCheckedChanged,
                    onCheckListItemDoneChanged = onCheckListItemDoneChanged
                )
            } else {
                taskRowsAgendaViewMode(
                    tasks = tasks.value,
                    onTaskClicked = navigateToAddEditTask,
                    onDoneCheckedChanged = onDoneCheckedChanged,
                    onCheckListItemDoneChanged = onCheckListItemDoneChanged
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ToDoAppTheme.spacing.medium),
            onClick = { navigateToAddEditTask(null) }
        ) {
            ToDoAppIcon(
                icon = ToDoAppIcons.icAdd,
                contentDescription = "add"
            )
        }
    }
}

@Composable
private fun DateHeader(
    date: LocalDate
) {
    Text(
        modifier = Modifier
            .padding(
                vertical = ToDoAppTheme.spacing.medium,
                horizontal = ToDoAppTheme.spacing.small
            ),
        text = if (date.isCurrentYear()) date.formatShortDayOfWeekDate() else date.formatLongDayOfWeekDate(),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

private fun LazyListScope.taskRowsDayViewMode(
    selectedDate: State<LocalDate>,
    tasks: List<Task>,
    onTaskClicked: (Task?) -> Unit,
    onDoneCheckedChanged: (Task, Boolean) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Task, Boolean) -> Unit
) {
    val oneTimeTask = tasks.filter { it.startDate == null && it.startTime == null }
    val routineTask = tasks.filter { it.startDate != null || it.startTime != null }

    items(oneTimeTask) { task ->
        TaskCard(
            task = task,
            onClick = { onTaskClicked(task) },
            onDoneCheckedChanged = { onDoneCheckedChanged(task, it) },
            onCheckListItemDoneChanged = { checkListItem, isDone -> onCheckListItemDoneChanged(checkListItem, task, isDone) }
        )
    }
    if (routineTask.isNotEmpty()) {
        item {
            DateHeader(date = selectedDate.value)
        }
    }
    items(routineTask) { task ->
        TaskCard(
            task = task,
            onClick = { onTaskClicked(task) },
            onDoneCheckedChanged = { onDoneCheckedChanged(task, it) },
            onCheckListItemDoneChanged = { checkListItem, isDone -> onCheckListItemDoneChanged(checkListItem, task, isDone) }
        )
    }
}

private fun LazyListScope.taskRowsAgendaViewMode(
    tasks: List<Task>,
    onTaskClicked: (Task?) -> Unit,
    onDoneCheckedChanged: (Task, Boolean) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Task, Boolean) -> Unit
) {
    itemsIndexed(
        items = tasks,
        key = { _, task -> task.id + (task.startDate?.toMillis() ?: 0L) }
    ) { index, task ->
        task.startDate?.let { date ->
            val previousDate = if (index > 0) tasks[index - 1].startDate else null

            if (date != previousDate) {
                DateHeader(date = date)
            }
        }

        TaskCard(
            task = task,
            onClick = { onTaskClicked(task) },
            onDoneCheckedChanged = { onDoneCheckedChanged(task, it) },
            onCheckListItemDoneChanged = { checkListItem, isDone -> onCheckListItemDoneChanged(checkListItem, task, isDone) }
        )
    }
}

class CalendarScreenPreviewProvider : PreviewParameterProvider<List<Task>> {
    override val values = sequenceOf(mockedTasks)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun CalendarScreenPreview(
    @PreviewParameter(CalendarScreenPreviewProvider::class) tasks: List<Task>
) {
    ToDoAppTheme {
        CalendarScreenContent(
            selectedDate = remember { mutableStateOf(getNowDate()) },
            calendarViewMode = remember { mutableStateOf(CalendarViewMode.DAY) },
            tasks = remember { mutableStateOf(tasks) },
            navigateToAddEditTask = {},
            onDoneCheckedChanged = { _, _ -> },
            onCheckListItemDoneChanged = { _, _, _ -> }
        )
    }
}
