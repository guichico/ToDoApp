package com.apphico.todoapp.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTasks
import com.apphico.designsystem.components.list.MainLazyList
import com.apphico.designsystem.task.TaskCard
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel,
    navigateToAddEditTask: (Task?) -> Unit,

    isCalendarExpanded: State<Boolean>,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit,
) {
    val calendar = calendarViewModel.calendar.collectAsState()
    val calendarViewMode = calendarViewModel.calendarViewMode.collectAsState()

    CalendarScreenContent(
        onCurrentMonthChanged = calendarViewModel::onCurrentMonthChanged,
        selectedDate = selectedDate,
        calendarViewMode = calendarViewMode,
        tasks = calendar,
        navigateToAddEditTask = navigateToAddEditTask,
        onDoneCheckedChanged = { task, isDone -> calendarViewModel.setTaskDone(task, isDone) },
        onCheckListItemDoneChanged = { checkListItem, task, isDone -> calendarViewModel.setCheckListItemDone(checkListItem, task, isDone) },

        isCalendarExpanded = isCalendarExpanded,
        onSelectedDateChanged = onSelectedDateChanged,
        isFilterExpanded = isFilterExpanded,
        selectedStatus = selectedStatus,
        onStatusChanged = onStatusChanged,
        groups = groups,
        selectedGroups = selectedGroups,
        onGroupSelected = onGroupSelected,
        onSearchClicked = onSearchClicked
    )
}

@Composable
private fun CalendarScreenContent(
    onCurrentMonthChanged: (Month?, Int?) -> Unit,
    selectedDate: State<LocalDate>,
    calendarViewMode: State<CalendarViewMode>,
    tasks: State<List<Task>>,
    navigateToAddEditTask: (Task?) -> Unit,
    onDoneCheckedChanged: (Task, Boolean) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, Task, Boolean) -> Unit,

    isCalendarExpanded: State<Boolean>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit,
) {
    val calendarListState = rememberLazyListState()

    LaunchedEffect(calendarListState) {
        snapshotFlow { calendarListState.firstVisibleItemIndex }
            .filter { index -> index < tasks.value.size && tasks.value.isNotEmpty() }
            .map { index ->
                val date = tasks.value[index].startDate
                date?.month to date?.year
            }
            .distinctUntilChanged()
            .collect { (month, year) ->
                onCurrentMonthChanged(month, year)
            }
    }

    MainLazyList(
        listState = calendarListState,
        onAddClicked = { navigateToAddEditTask(null) },
        isCalendarExpanded = isCalendarExpanded,
        selectedDate = selectedDate,
        onSelectedDateChanged = onSelectedDateChanged,
        isFilterExpanded = isFilterExpanded,
        selectedStatus = selectedStatus,
        onStatusChanged = onStatusChanged,
        groups = groups,
        selectedGroups = selectedGroups,
        onGroupSelected = onGroupSelected,
        onSearchClicked = onSearchClicked
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
}

@Composable
private fun DateHeader(
    date: LocalDate
) {
    val day = DateTimeFormatter.ofPattern("d").format(date)
    val dayOfWeek = DateTimeFormatter.ofPattern("E").format(date)

    Column(
        modifier = Modifier
            .padding(
                vertical = ToDoAppTheme.spacing.large,
                horizontal = ToDoAppTheme.spacing.small
            )
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
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
        key = { _, task -> task.key() }
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

@PreviewLightDark
@Composable
private fun CalendarScreenPreview(
    @PreviewParameter(CalendarScreenPreviewProvider::class) tasks: List<Task>
) {
    /*
    ToDoAppTheme {
        CalendarScreenContent(
            onCurrentMonthChanged = { _, _ -> },
            selectedDate = remember { mutableStateOf(getNowDate()) },
            calendarViewMode = remember { mutableStateOf(CalendarViewMode.DAY) },
            tasks = remember { mutableStateOf(tasks) },
            navigateToAddEditTask = {},
            onDoneCheckedChanged = { _, _ -> },
            onCheckListItemDoneChanged = { _, _, _ -> }
        )
    }
     */
}
