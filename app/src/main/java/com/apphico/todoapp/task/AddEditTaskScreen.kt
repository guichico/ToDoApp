package com.apphico.todoapp.task

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Location
import com.apphico.core_model.RecurringTask
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTask
import com.apphico.designsystem.R
import com.apphico.designsystem.animatedElevation
import com.apphico.designsystem.components.card.AddEditHeader
import com.apphico.designsystem.components.checklist.CreateCheckList
import com.apphico.designsystem.components.date.DaysOfWeekGrid
import com.apphico.designsystem.components.dialogs.CheckBoxDialog
import com.apphico.designsystem.components.dialogs.DateDialog
import com.apphico.designsystem.components.dialogs.TimeDialog
import com.apphico.designsystem.components.dialogs.showDiscardChangesDialogOnBackIfNeed
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

    val editingCheckList = addEditTaskViewModel.editingCheckList.collectAsState()

    val nameError = addEditTaskViewModel.nameError.collectAsState()
    val startDateError = addEditTaskViewModel.startDateError.collectAsState()

    val taskSaveSuccess = stringResource(R.string.task_saved)
    val taskSaveError = stringResource(R.string.task_save_error)

    val taskDeleteSuccess = stringResource(R.string.task_deleted)
    val taskDeleteError = stringResource(R.string.task_delete_error)

    val showDiscardChangesDialogOnBackIfNeed = showDiscardChangesDialogOnBackIfNeed(
        hasChanges = addEditTaskViewModel::hasChanges,
        navigateBack = navigateBack
    )

    var saveMethod by remember { mutableStateOf<RecurringTask>(RecurringTask.ThisTask) }
    var isSaveDialogOpen by remember { mutableStateOf(false) }

    val saveAction = {
        addEditTaskViewModel.save(
            saveMethod = saveMethod
        ) { isSuccess ->
            snackBar(if (isSuccess) taskSaveSuccess else taskSaveError)
            if (isSuccess) navigateBack()
        }
    }

    var deleteMethod by remember { mutableStateOf<RecurringTask>(RecurringTask.ThisTask) }
    var isDeleteDialogOpen by remember { mutableStateOf(false) }

    val deleteAction = {
        addEditTaskViewModel.delete(
            deleteMethod = deleteMethod
        ) { isSuccess ->
            snackBar(if (isSuccess) taskDeleteSuccess else taskDeleteError)
            if (isSuccess) navigateBack()
        }
    }

    if (isSaveDialogOpen) {
        CheckBoxDialog(
            title = stringResource(R.string.save_recurring_task),
            values = listOf(
                RecurringTask.ThisTask,
                RecurringTask.Future,
                RecurringTask.All
            ),
            selectedItem = saveMethod,
            onItemSelected = {
                saveMethod = it as RecurringTask
            },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissRequest = { isSaveDialogOpen = false },
            confirmButtonText = stringResource(R.string.save),
            onConfirmClicked = {
                isSaveDialogOpen = false
                saveAction()
            }
        )
    }

    if (isDeleteDialogOpen) {
        CheckBoxDialog(
            title = stringResource(R.string.delete_recurring_task),
            values = listOf(
                RecurringTask.ThisTask,
                RecurringTask.Future,
                RecurringTask.All
            ),
            selectedItem = deleteMethod,
            onItemSelected = {
                deleteMethod = it as RecurringTask
            },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissRequest = { isDeleteDialogOpen = false },
            confirmButtonText = stringResource(R.string.delete),
            onConfirmClicked = {
                isDeleteDialogOpen = false
                deleteAction()
            }
        )
    }

    val scrollState = rememberScrollState()
    val showElevation = remember {
        derivedStateOf { scrollState.isScrollInProgress || scrollState.value != 0 }
    }

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
            val task = editingTask.value

            if (isEditing && task.isRepeatable()) {
                isSaveDialogOpen = true
            } else {
                saveAction()
            }
        },
        onDeleteClicked = {
            val task = editingTask.value

            if (task.isRepeatable()) {
                isDeleteDialogOpen = true
            } else {
                deleteAction()
            }
        },
        onCopyClicked = {},
        navigateBack = {
            showDiscardChangesDialogOnBackIfNeed()
        }
    ) { innerPadding ->
        AddTaskScreenContent(
            innerPadding = innerPadding,
            scrollState = scrollState,
            task = editingTask,
            checkList = editingCheckList,
            nameError = nameError,
            onNameChange = addEditTaskViewModel::onNameChanged,
            onDescriptionChange = addEditTaskViewModel::onDescriptionChanged,
            navigateToSelectGroup = navigateToSelectGroup,
            onGroupRemoved = addEditTaskViewModel::onGroupRemoved,
            startDateError = startDateError,
            onStartDateChanged = addEditTaskViewModel::onStartDateChanged,
            onStartTimeChanged = addEditTaskViewModel::onStartTimeChanged,
            onEndDateChanged = addEditTaskViewModel::onEndDateChanged,
            onEndTimeChanged = addEditTaskViewModel::onEndTimeChanged,
            onDaysOfWeekChanged = addEditTaskViewModel::onDaysOfWeekChanged,
            onCheckListItemChanged = addEditTaskViewModel::onCheckListItemChanged,
            onCheckListItemItemAdded = addEditTaskViewModel::onCheckListItemItemAdded,
            onCheckListItemItemRemoved = addEditTaskViewModel::onCheckListItemItemRemoved,
            onCheckListItemDoneChanged = addEditTaskViewModel::setCheckListItemDone,
            onReminderTimeChanged = addEditTaskViewModel::onReminderTimeChanged,
            navigateToSelectLocation = navigateToSelectLocation
        )
    }
}

@Composable
private fun AddTaskScreenContent(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    task: State<Task>,
    nameError: State<Int?>,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    navigateToSelectGroup: () -> Unit,
    onGroupRemoved: () -> Unit,
    startDateError: State<Int?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    onStartTimeChanged: (LocalTime) -> Unit,
    onEndDateChanged: (LocalDate?) -> Unit,
    onEndTimeChanged: (LocalTime) -> Unit,
    onDaysOfWeekChanged: (List<Int>) -> Unit,
    checkList: State<List<CheckListItem>>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,
    onReminderTimeChanged: (LocalTime?) -> Unit,
    navigateToSelectLocation: (Location?) -> Unit
) {
    Box(
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
                nameError = nameError,
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
                startDateError = startDateError,
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
                checkList = checkList,
                parentDate = remember { derivedStateOf { task.value.startDate } },
                onCheckListItemChanged = onCheckListItemChanged,
                onCheckListItemItemAdded = onCheckListItemItemAdded,
                onCheckListItemItemRemoved = onCheckListItemItemRemoved,
                onCheckListItemDoneChanged = onCheckListItemDoneChanged
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
                navigateToSelectLocation = navigateToSelectLocation
            )
        }
    }
}

@Composable
private fun Dates(
    startDate: State<LocalDate?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    startDateError: State<Int?>,
    startTime: State<LocalTime?>,
    onStartTimeChanged: (LocalTime) -> Unit,
    endDate: State<LocalDate?>,
    onEndDateChanged: (LocalDate?) -> Unit,
    endTime: State<LocalTime?>,
    onEndTimeChanged: (LocalTime) -> Unit
) {
    Column {
        StarDateRow(
            startDate = startDate,
            startDateError = startDateError,
            onStartDateChanged = onStartDateChanged,
            startTime = startTime,
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
    startDateError: State<Int?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    startTime: State<LocalTime?>,
    onStartTimeChanged: (LocalTime) -> Unit
) {
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate.value?.toMillis() ?: getGMTNowMillis()
    )
    val startTimePickerState = rememberTimePickerState(
        initialHour = startTime.value?.hour ?: LocalTime.now().hour,
        initialMinute = startTime.value?.minute ?: 0
    )

    DateRow(
        date = startDate,
        datePlaceholder = stringResource(R.string.start_date),
        datePickerState = startDatePickerState,
        onDateChanged = onStartDateChanged,
        dateError = startDateError,
        time = startTime,
        timePickerState = startTimePickerState,
        onTimeChanged = onStartTimeChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EndDateRow(
    endDate: State<LocalDate?>,
    onEndDateChanged: (LocalDate?) -> Unit,
    endTime: State<LocalTime?>,
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

    DateRow(
        date = endDate,
        datePlaceholder = stringResource(R.string.end_date),
        datePickerState = endDatePickerState,
        onDateChanged = onEndDateChanged,
        dateError = remember { mutableStateOf(null) },
        time = endTime,
        timePickerState = endTimePickerState,
        onTimeChanged = onEndTimeChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRow(
    date: State<LocalDate?>,
    datePlaceholder: String,
    datePickerState: DatePickerState,
    onDateChanged: (LocalDate?) -> Unit,
    dateError: State<Int?>,
    time: State<LocalTime?>,
    timePickerState: TimePickerState,
    onTimeChanged: (LocalTime) -> Unit,
) {
    val isStartDatePickerDialogOpen = remember { mutableStateOf(false) }
    val isStartTimePickerDialogOpen = remember { mutableStateOf(false) }

    if (isStartDatePickerDialogOpen.value) {
        DateDialog(
            isDatePickerDialogOpen = isStartDatePickerDialogOpen,
            datePickerState = datePickerState,
            onDateChanged = onDateChanged
        )
    }

    if (isStartTimePickerDialogOpen.value) {
        TimeDialog(
            isTimePickerDialogOpen = isStartTimePickerDialogOpen,
            timePickerState = timePickerState,
            onTimeChanged = onTimeChanged
        )
    }

    Column {
        Row {
            NormalTextField(
                modifier = Modifier
                    .weight(0.6f),
                value = date.value?.formatMediumDate() ?: "",
                placeholder = datePlaceholder,
                onClick = { isStartDatePickerDialogOpen.value = true }
            )
            Spacer(modifier = Modifier.weight(0.02f))
            NormalTextField(
                modifier = Modifier
                    .weight(0.4f),
                value = time.value?.formatShortTime() ?: "",
                placeholder = stringResource(R.string.hour),
                onClick = { isStartTimePickerDialogOpen.value = true }
            )
        }
        if (dateError.value != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = ToDoAppTheme.spacing.extraSmall,
                        horizontal = ToDoAppTheme.spacing.small
                    ),
                text = dateError.value?.let { stringResource(it) } ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

@Composable
private fun CheckList(
    scrollState: ScrollState,
    checkList: State<List<CheckListItem>>,
    parentDate: State<LocalDate?>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit
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
        parentDate = parentDate,
        onCheckListItemChanged = onCheckListItemChanged,
        onCheckListItemItemAdded = onCheckListItemItemAdded,
        onCheckListItemItemRemoved = onCheckListItemItemRemoved,
        onCheckListItemDoneChanged = onCheckListItemDoneChanged
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

@PreviewLightDark
@Composable
private fun AddTaskScreenPreview(
    @PreviewParameter(AddTaskScreenPreviewProvider::class) task: Task
) {
    ToDoAppTheme {
        AddTaskScreenContent(
            innerPadding = PaddingValues(),
            scrollState = ScrollState(0),
            task = remember { mutableStateOf(task) },
            nameError = remember { mutableStateOf(null) },
            onNameChange = {},
            onDescriptionChange = {},
            navigateToSelectGroup = {},
            onGroupRemoved = {},
            startDateError = remember { mutableStateOf(null) },
            onStartDateChanged = {},
            onStartTimeChanged = {},
            onEndDateChanged = {},
            onEndTimeChanged = {},
            onDaysOfWeekChanged = {},
            checkList = remember { mutableStateOf(emptyList()) },
            onCheckListItemChanged = { _, _ -> },
            onCheckListItemItemAdded = {},
            onCheckListItemItemRemoved = {},
            onCheckListItemDoneChanged = { _, _, _ -> },
            onReminderTimeChanged = {},
            navigateToSelectLocation = {}
        )
    }
}
