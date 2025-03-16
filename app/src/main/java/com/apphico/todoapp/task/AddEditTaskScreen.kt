package com.apphico.todoapp.task

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTask
import com.apphico.designsystem.R
import com.apphico.designsystem.animatedElevation
import com.apphico.designsystem.components.card.AddEditHeader
import com.apphico.designsystem.components.checklist.CreateCheckList
import com.apphico.designsystem.components.date.DaysOfWeekGrid
import com.apphico.designsystem.components.dialogs.DateDialog
import com.apphico.designsystem.components.dialogs.DiscardChangesDialog
import com.apphico.designsystem.components.dialogs.TimeDialog
import com.apphico.designsystem.components.dialogs.navigateBackConfirm
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.textfield.LocationField
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatMediumDate
import com.apphico.extensions.formatShortTime
import com.apphico.extensions.getGMTNowMillis
import com.apphico.extensions.getNowDate
import com.apphico.extensions.toMillis
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddEditTaskScreen(
    addEditTaskViewModel: AddEditTaskViewModel = hiltViewModel(),
    snackBar: (String) -> Unit,
    navigateToSelectGroup: () -> Unit,
    navigateToSelectLocation: (Location?) -> Unit,
    navigateBack: () -> Unit
) {
    val editingTask = addEditTaskViewModel.editingTask.collectAsState()
    val isEditing = addEditTaskViewModel.isEditing

    val isAlertDialogOpen = remember { mutableStateOf(false) }
    val hasChanges = remember { derivedStateOf { addEditTaskViewModel.hasChanges() } }

    DiscardChangesDialog(
        isAlertDialogOpen = isAlertDialogOpen,
        hasChanges = hasChanges,
        navigateBack = navigateBack
    )

    val scrollState = rememberScrollState()
    val showElevation = remember {
        derivedStateOf { scrollState.isScrollInProgress || scrollState.value != 0 }
    }

    val taskSaveSuccess = stringResource(R.string.task_saved)
    val taskSaveError = stringResource(R.string.task_save_error)

    val taskDeleteSuccess = stringResource(R.string.task_deleted)
    val taskDeleteError = stringResource(R.string.task_delete_error)

    DeleteSaveTopBar(
        modifier = Modifier
            .fillMaxWidth()
            .animatedElevation(
                conditionState = showElevation,
                shadowElevation = 10f
            )
            .zIndex(1f),
        title = stringResource(R.string.add_new_task),
        isEditing = isEditing,
        onSaveClicked = {
            addEditTaskViewModel.save { isSuccess ->
                snackBar(if (isSuccess) taskSaveSuccess else taskSaveError)
                navigateBack()
            }
        },
        onDeleteClicked = {
            addEditTaskViewModel.delete { isSuccess ->
                snackBar(if (isSuccess) taskDeleteSuccess else taskDeleteError)
                navigateBack()
            }
        },
        navigateBack = {
            navigateBackConfirm(
                isAlertDialogOpen = isAlertDialogOpen,
                hasChanges = hasChanges,
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        AddTaskScreenContent(
            innerPadding = innerPadding,
            scrollState = scrollState,
            task = editingTask,
            onNameChange = addEditTaskViewModel::onNameChanged,
            onDescriptionChange = addEditTaskViewModel::onDescriptionChanged,
            navigateToSelectGroup = navigateToSelectGroup,
            onGroupRemoved = addEditTaskViewModel::onGroupRemoved,
            onStartDateChanged = addEditTaskViewModel::onStartDateChanged,
            onStartTimeChanged = addEditTaskViewModel::onStartTimeChanged,
            onEndDateChanged = addEditTaskViewModel::onEndDateChanged,
            onEndTimeChanged = addEditTaskViewModel::onEndTimeChanged,
            onDaysOfWeekChanged = addEditTaskViewModel::onDaysOfWeekChanged,
            onCheckListChanged = addEditTaskViewModel::onCheckListChanged,
            onReminderTimeChanged = addEditTaskViewModel::onReminderTimeChanged,
            navigateToSelectLocation = navigateToSelectLocation,
            onLocationRemoved = addEditTaskViewModel::onLocationRemoved
        )
    }
}

@Composable
private fun AddTaskScreenContent(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    task: State<Task>,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    navigateToSelectGroup: () -> Unit,
    onGroupRemoved: () -> Unit,
    onStartDateChanged: (LocalDate?) -> Unit,
    onStartTimeChanged: (LocalTime) -> Unit,
    onEndDateChanged: (LocalDate?) -> Unit,
    onEndTimeChanged: (LocalTime) -> Unit,
    onDaysOfWeekChanged: (List<Int>) -> Unit,
    onCheckListChanged: (List<CheckListItem>) -> Unit,
    onReminderTimeChanged: (LocalTime?) -> Unit,
    navigateToSelectLocation: (Location?) -> Unit,
    onLocationRemoved: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = ToDoAppTheme.spacing.extraLarge,
                    horizontal = ToDoAppTheme.spacing.large
                )
                .imePadding()
        ) {
            AddEditHeader(
                nameValue = remember { derivedStateOf { task.value.name } },
                namePlaceholder = stringResource(id = R.string.title),
                onNameChange = onNameChange,
                descriptionValue = remember { derivedStateOf { task.value.description } },
                descriptionPlaceholder = stringResource(id = R.string.description),
                onDescriptionChange = onDescriptionChange,
                group = remember { derivedStateOf { task.value.group } },
                groupPlaceholder = stringResource(id = R.string.select_group),
                navigateToSelectGroup = navigateToSelectGroup,
                onGroupRemoved = onGroupRemoved
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

            Dates(
                startDate = remember { derivedStateOf { task.value.startDate } },
                startTime = remember { derivedStateOf { task.value.startTime } },
                endDate = remember { derivedStateOf { task.value.endDate } },
                endTime = remember { derivedStateOf { task.value.endTime } },
                onStartDateChanged = onStartDateChanged,
                onStartTimeChanged = onStartTimeChanged,
                onEndDateChanged = onEndDateChanged,
                onEndTimeChanged = onEndTimeChanged
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

            Text(
                modifier = Modifier
                    .padding(vertical = ToDoAppTheme.spacing.extraSmall),
                text = stringResource(R.string.repeat),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.extraLarge))

            DaysOfWeekGrid(
                selectedDaysState = remember { derivedStateOf { task.value.daysOfWeek } },
                onSelectionChanged = onDaysOfWeekChanged
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.extraExtraLarge))

            CheckList(
                scrollState = scrollState,
                checkList = remember { derivedStateOf { task.value.checkList } },
                onCheckListChanged = onCheckListChanged
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

            Reminder(
                reminder = remember { derivedStateOf { task.value.reminder } },
                onReminderTimeChanged = onReminderTimeChanged
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

            Text(
                modifier = Modifier
                    .padding(vertical = ToDoAppTheme.spacing.extraSmall),
                text = stringResource(R.string.location),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

            LocationField(
                modifier = Modifier.fillMaxWidth(),
                task = task,
                navigateToSelectLocation = navigateToSelectLocation,
                onLocationRemoved = onLocationRemoved
            )
        }
    }
}

@Composable
private fun Dates(
    startDate: State<LocalDate?>,
    startTime: State<LocalTime?>,
    endDate: State<LocalDate?>,
    endTime: State<LocalTime?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    onStartTimeChanged: (LocalTime) -> Unit,
    onEndDateChanged: (LocalDate?) -> Unit,
    onEndTimeChanged: (LocalTime) -> Unit
) {
    Column {
        StarDateRow(
            startDate = startDate,
            startTime = startTime,
            onStartDateChanged = onStartDateChanged,
            onStartTimeChanged = onStartTimeChanged
        )
        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))
        EndDateRow(
            endDate = endDate,
            endTime = endTime,
            onEndDateChanged = onEndDateChanged,
            onEndTimeChanged = onEndTimeChanged
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StarDateRow(
    startDate: State<LocalDate?>,
    startTime: State<LocalTime?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    onStartTimeChanged: (LocalTime) -> Unit
) {
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate.value?.toMillis() ?: getGMTNowMillis()
    )
    val startTimePickerState = rememberTimePickerState(
        initialHour = startTime.value?.hour ?: LocalTime.now().hour,
        initialMinute = startTime.value?.minute ?: 0
    )

    val isStartDatePickerDialogOpen = remember { mutableStateOf(false) }
    val isStartTimePickerDialogOpen = remember { mutableStateOf(false) }

    if (isStartDatePickerDialogOpen.value) {
        DateDialog(
            isDatePickerDialogOpen = isStartDatePickerDialogOpen,
            datePickerState = startDatePickerState,
            onDateChanged = onStartDateChanged
        )
    }

    if (isStartTimePickerDialogOpen.value) {
        TimeDialog(
            isTimePickerDialogOpen = isStartTimePickerDialogOpen,
            timePickerState = startTimePickerState,
            onTimeChanged = onStartTimeChanged
        )
    }

    Row {
        NormalTextField(
            modifier = Modifier
                .weight(0.6f),
            value = startDate.value?.formatMediumDate() ?: "",
            placeholder = stringResource(R.string.start_date),
            onClick = { isStartDatePickerDialogOpen.value = true }
        )
        Spacer(modifier = Modifier.weight(0.02f))
        NormalTextField(
            modifier = Modifier
                .weight(0.4f),
            value = startTime.value?.formatShortTime() ?: "",
            placeholder = stringResource(R.string.hour),
            onClick = { isStartTimePickerDialogOpen.value = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EndDateRow(
    endDate: State<LocalDate?>,
    endTime: State<LocalTime?>,
    onEndDateChanged: (LocalDate?) -> Unit,
    onEndTimeChanged: (LocalTime) -> Unit
) {
    var initialEndHour = endTime.value?.hour ?: LocalTime.now().hour.plus(1)
    var initialEndDate = endDate.value ?: getNowDate()

    if (initialEndHour >= 24) {
        initialEndDate = initialEndDate.plusDays(1)
        initialEndHour = 0
    }

    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialEndDate.toMillis()
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = initialEndHour,
        initialMinute = endTime.value?.minute ?: 0
    )

    val isEndDatePickerDialogOpen = remember { mutableStateOf(false) }
    val isEndTimePickerDialogOpen = remember { mutableStateOf(false) }

    if (isEndDatePickerDialogOpen.value) {
        DateDialog(
            isDatePickerDialogOpen = isEndDatePickerDialogOpen,
            datePickerState = endDatePickerState,
            onDateChanged = onEndDateChanged
        )
    }

    if (isEndTimePickerDialogOpen.value) {
        TimeDialog(
            isTimePickerDialogOpen = isEndTimePickerDialogOpen,
            timePickerState = endTimePickerState,
            onTimeChanged = onEndTimeChanged
        )
    }
    Row {
        NormalTextField(
            modifier = Modifier
                .weight(0.6f),
            value = endDate.value?.formatMediumDate() ?: "",
            placeholder = stringResource(R.string.end_date),
            onClick = { isEndDatePickerDialogOpen.value = true }
        )
        Spacer(modifier = Modifier.weight(0.02f))
        NormalTextField(
            modifier = Modifier
                .weight(0.4f),
            value = endTime.value?.formatShortTime() ?: "",
            placeholder = stringResource(R.string.hour),
            onClick = { isEndTimePickerDialogOpen.value = true }
        )
    }
}

@Composable
private fun CheckList(
    scrollState: ScrollState,
    checkList: State<List<CheckListItem>>,
    onCheckListChanged: (List<CheckListItem>) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(vertical = ToDoAppTheme.spacing.extraSmall),
        text = stringResource(R.string.checklist_header_title),
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

    CreateCheckList(
        scrollState = scrollState,
        addNewItemTitle = stringResource(R.string.add_checklist_item),
        checkList = checkList,
        onCheckListChanged = onCheckListChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Reminder(
    reminder: State<LocalTime?>,
    onReminderTimeChanged: (LocalTime?) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(vertical = ToDoAppTheme.spacing.extraSmall),
        text = stringResource(R.string.reminder),
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

    val reminderTimePickerState = rememberTimePickerState(
        initialHour = reminder.value?.hour ?: 0,
        initialMinute = reminder.value?.minute ?: 0
    )
    val isReminderTimePickerDialogOpen = remember { mutableStateOf(false) }

    if (isReminderTimePickerDialogOpen.value) {
        TimeDialog(
            isTimePickerDialogOpen = isReminderTimePickerDialogOpen,
            timePickerState = reminderTimePickerState,
            onTimeChanged = onReminderTimeChanged
        )
    }

    SmallTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = reminder.value?.formatShortTime() ?: "",
        placeholder = stringResource(R.string.add_reminder),
        onClick = { isReminderTimePickerDialogOpen.value = true },
        leadingIcon = {
            ToDoAppIcon(
                icon = ToDoAppIcons.icReminder,
                contentDescription = "reminder"
            )
        },
        trailingIcon = {
            reminder.value?.let {
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icRemove,
                    onClick = { onReminderTimeChanged(null) }
                )
            }
        }
    )
}

class AddTaskScreenPreviewProvider : PreviewParameterProvider<Task> {
    override val values = sequenceOf(
        Task(
            startDate = LocalDate.now(),
            startTime = LocalTime.now(),
            endDate = LocalDate.now(),
            endTime = LocalTime.now()
        ),
        mockedTask
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun AddTaskScreenPreview(
    @PreviewParameter(AddTaskScreenPreviewProvider::class) task: Task
) {
    ToDoAppTheme {
        AddTaskScreenContent(
            innerPadding = PaddingValues(),
            scrollState = ScrollState(0),
            task = remember { mutableStateOf(task) },
            onNameChange = {},
            onDescriptionChange = {},
            navigateToSelectGroup = {},
            onGroupRemoved = {},
            onStartDateChanged = {},
            onStartTimeChanged = {},
            onEndDateChanged = {},
            onEndTimeChanged = {},
            onDaysOfWeekChanged = {},
            onCheckListChanged = {},
            onReminderTimeChanged = {},
            navigateToSelectLocation = {},
            onLocationRemoved = {}
        )
    }
}
