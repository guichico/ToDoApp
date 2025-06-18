package com.apphico.todoapp.calendar

import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTasks
import com.apphico.designsystem.components.list.MainLazyList
import com.apphico.designsystem.task.TaskCard
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import com.apphico.todoapp.ad.BannerAdView
import com.apphico.todoapp.ad.ToDoAppBannerAd
import com.google.android.gms.ads.AdView
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
    selectedDate: State<LocalDate>,
    anchorViewHeight: State<Dp>,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    val wasWelcomeClosed by calendarViewModel.wasWelcomeClosed.collectAsState(true)

    val calendar = calendarViewModel.calendar.collectAsState()
    val calendarViewMode = calendarViewModel.calendarViewMode.collectAsState()

    if (!wasWelcomeClosed) {
        WelcomeDialog(
            onExploreClicked = {
                calendarViewModel.setWasWelcomeClosed()
            }
        )
    }

    CalendarScreenContent(
        onCurrentMonthChanged = calendarViewModel::onCurrentMonthChanged,
        selectedDate = selectedDate,
        calendarViewMode = calendarViewMode,
        tasks = calendar,
        navigateToAddEditTask = navigateToAddEditTask,
        onDoneCheckedChanged = { task, isDone -> calendarViewModel.setTaskDone(task, isDone) },
        onCheckListItemDoneChanged = { checkListItem, task, isDone -> calendarViewModel.setCheckListItemDone(checkListItem, task, isDone) },
        anchorViewHeight = anchorViewHeight,
        isNestedViewExpanded = isNestedViewExpanded,
        onNestedViewClosed = onNestedViewClosed,
        nestedContent = nestedContent
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
    anchorViewHeight: State<Dp>,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
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

    val activity = LocalActivity.current

    var loadedAd by remember { mutableStateOf<AdView?>(null) }

    LaunchedEffect(Unit) {
        activity?.let {
            if (loadedAd == null) {
                loadedAd = ToDoAppBannerAd(activity).getAnchoredAdView()
            }
        }
    }

    DisposableEffect(Unit) {
        // Destroy the AdView to prevent memory leaks when the screen is disposed.
        onDispose {
            loadedAd?.let {
                if (it.parent is ViewGroup) {
                    (it.parent as ViewGroup).removeView(loadedAd)
                }
                it.destroy()
                loadedAd = null
            }
        }
    }

    MainLazyList(
        listState = calendarListState,
        onAddClicked = { navigateToAddEditTask(null) },
        anchorViewHeight = anchorViewHeight,
        isNestedViewExpanded = isNestedViewExpanded,
        onNestedViewClosed = onNestedViewClosed,
        nestedContent = nestedContent
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
                loadedAd = loadedAd,
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
    loadedAd: AdView?,
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

        if (index != 0 && (index == 8 || index % 30 == 0)) {
            loadedAd?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = ToDoAppTheme.spacing.large)
                ) {
                    BannerAdView(adView = it)
                }
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
    ToDoAppTheme {
        CalendarScreenContent(
            onCurrentMonthChanged = { _, _ -> },
            selectedDate = remember { mutableStateOf(getNowDate()) },
            calendarViewMode = remember { mutableStateOf(CalendarViewMode.DAY) },
            tasks = remember { mutableStateOf(tasks) },
            navigateToAddEditTask = {},
            onDoneCheckedChanged = { _, _ -> },
            onCheckListItemDoneChanged = { _, _, _ -> },
            anchorViewHeight = remember { mutableStateOf(342.dp) },
            isNestedViewExpanded = remember { mutableStateOf(false) },
            onNestedViewClosed = {},
            nestedContent = {}
        )
    }
}
