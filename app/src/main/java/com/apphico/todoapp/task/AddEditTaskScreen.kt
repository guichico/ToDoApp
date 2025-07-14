package com.apphico.todoapp.task

import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Location
import com.apphico.core_model.RecurringTask
import com.apphico.core_model.Reminder
import com.apphico.core_model.Task
import com.apphico.core_model.fakeData.mockedTask
import com.apphico.designsystem.R
import com.apphico.designsystem.animatedElevation
import com.apphico.designsystem.components.buttons.SmallButton
import com.apphico.designsystem.components.card.AddEditHeader
import com.apphico.designsystem.components.checklist.CreateCheckList
import com.apphico.designsystem.components.date.DaysOfWeekGrid
import com.apphico.designsystem.components.dialogs.CheckBoxDialog
import com.apphico.designsystem.components.dialogs.DateDialog
import com.apphico.designsystem.components.dialogs.DefaultDialog
import com.apphico.designsystem.components.dialogs.ReminderDialog
import com.apphico.designsystem.components.dialogs.TimeDialog
import com.apphico.designsystem.components.dialogs.showDiscardChangesDialogOnBackIfNeed
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.textfield.LocationField
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.reminder.CheckNotificationPermission
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatMediumDate
import com.apphico.extensions.formatMediumDateAndTime
import com.apphico.extensions.formatShortTime
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowGMTMillis
import com.apphico.extensions.getNowTime
import com.apphico.extensions.toMillis
import com.apphico.extensions.toUTCMillis
import com.apphico.todoapp.ToDoAppBootReceiver
import com.apphico.todoapp.ad.BannerAdView
import com.apphico.todoapp.ad.ToDoAppBannerAd
import com.google.android.gms.ads.AdView
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
    val wasDatesExplanationClosed = addEditTaskViewModel.wasDatesExplanationClosed.collectAsState(true)

    val editingTask = addEditTaskViewModel.editingTask.collectAsState()
    val editingCheckList = addEditTaskViewModel.editingCheckList.collectAsState()

    val isEditing = addEditTaskViewModel.isEditing

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

    val recurringTask = listOfNotNull(
        RecurringTask.ThisTask,
        RecurringTask.Future,
        if (addEditTaskViewModel.canSaveAll()) RecurringTask.All else null
    )

    if (isSaveDialogOpen) {
        CheckBoxDialog(
            title = stringResource(R.string.save_recurring_task),
            values = recurringTask,
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
        onCopyClicked = {
            addEditTaskViewModel.copy { isSuccess ->
                snackBar(if (isSuccess) taskSaveSuccess else taskSaveError)
                if (isSuccess) navigateBack()
            }
        },
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
            wasDatesExplanationClosed = wasDatesExplanationClosed,
            onWasDatesExplanationClosed = { addEditTaskViewModel.setWasDatesExplanationClosed() },
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
            onReminderChanged = addEditTaskViewModel::onReminderChanged,
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
    wasDatesExplanationClosed: State<Boolean>,
    onWasDatesExplanationClosed: () -> Unit,
    startDateError: State<Int?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    onStartTimeChanged: (LocalTime?) -> Unit,
    onEndDateChanged: (LocalDate?) -> Unit,
    onEndTimeChanged: (LocalTime?) -> Unit,
    onDaysOfWeekChanged: (List<Int>) -> Unit,
    checkList: State<List<CheckListItem>>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,
    onReminderChanged: (Reminder?) -> Unit,
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
                wasDatesExplanationClosed = wasDatesExplanationClosed,
                onWasDatesExplanationClosed = onWasDatesExplanationClosed,
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
                color = MaterialTheme.colorScheme.primary,
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

            if (wasDatesExplanationClosed.value) {
                // Banner ad view
                LocalActivity.current?.let {
                    var adView by remember { mutableStateOf<AdView?>(null) }

                    if (adView == null) {
                        adView = ToDoAppBannerAd(it).getAdaptiveAdView(LocalConfiguration.current.screenWidthDp)
                    }

                    adView?.let {
                        BannerAdView(adView = it)
                        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

                        DisposableEffect(Unit) {
                            // Destroy the AdView to prevent memory leaks when the screen is disposed.
                            onDispose {
                                if (it.parent is ViewGroup) {
                                    (it.parent as ViewGroup).removeView(adView)
                                }
                                it.destroy()
                                adView = null
                            }
                        }
                    }
                }
            }

            ReminderField(
                task = task,
                onReminderChanged = onReminderChanged
            )

            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

            Text(
                modifier = Modifier
                    .padding(vertical = ToDoAppTheme.spacing.extraSmall),
                text = stringResource(R.string.location),
                color = MaterialTheme.colorScheme.primary,
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
    wasDatesExplanationClosed: State<Boolean>,
    onWasDatesExplanationClosed: () -> Unit,
    startDate: State<LocalDate?>,
    onStartDateChanged: (LocalDate?) -> Unit,
    startDateError: State<Int?>,
    startTime: State<LocalTime?>,
    onStartTimeChanged: (LocalTime?) -> Unit,
    endDate: State<LocalDate?>,
    onEndDateChanged: (LocalDate?) -> Unit,
    endTime: State<LocalTime?>,
    onEndTimeChanged: (LocalTime?) -> Unit
) {
    Column {
        if (!wasDatesExplanationClosed.value) {
            DatesExplanationDialog(
                onWasDatesExplanationClosed = onWasDatesExplanationClosed
            )
        }

        StarDateRow(
            startDate = startDate,
            startDateError = startDateError,
            onStartDateChanged = onStartDateChanged,
            startTime = startTime,
            onStartTimeChanged = onStartTimeChanged
        )
        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))
        EndDateRow(
            startDate = startDate,
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
    onStartTimeChanged: (LocalTime?) -> Unit
) {
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate.value?.toMillis() ?: getNowGMTMillis()
    )
    val startTimePickerState = rememberTimePickerState(
        initialHour = startTime.value?.hour ?: getNowTime().hour,
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
    startDate: State<LocalDate?>,
    endDate: State<LocalDate?>,
    onEndDateChanged: (LocalDate?) -> Unit,
    endTime: State<LocalTime?>,
    onEndTimeChanged: (LocalTime?) -> Unit
) {
    var initialEndHour = endTime.value?.hour ?: getNowTime().hour.plus(1)
    var initialEndDate = endDate.value ?: getNowDate()

    if (initialEndHour >= 24) {
        initialEndDate = initialEndDate.plusDays(1)
        initialEndHour = 0
    }

    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialEndDate.toMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= (startDate.value?.toUTCMillis() ?: 0)
            }
        }
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = initialEndHour,
        initialMinute = endTime.value?.minute ?: 0
    )

    LaunchedEffect(endTime.value) {
        endTimePickerState.hour = initialEndHour
        endTimePickerState.minute = endTime.value?.minute ?: 0
    }

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
    onTimeChanged: (LocalTime?) -> Unit,
) {
    val isDatePickerDialogOpen = remember { mutableStateOf(false) }
    val isTimePickerDialogOpen = remember { mutableStateOf(false) }

    if (isDatePickerDialogOpen.value) {
        DateDialog(
            isDatePickerDialogOpen = isDatePickerDialogOpen,
            datePickerState = datePickerState,
            onDateChanged = onDateChanged
        )
    }

    if (isTimePickerDialogOpen.value) {
        TimeDialog(
            isTimePickerDialogOpen = isTimePickerDialogOpen,
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
                onClick = { isDatePickerDialogOpen.value = true }
            )
            Spacer(modifier = Modifier.weight(0.02f))
            NormalTextField(
                modifier = Modifier
                    .weight(0.4f),
                value = time.value?.formatShortTime() ?: "",
                placeholder = stringResource(R.string.hour),
                onClick = { isTimePickerDialogOpen.value = true }
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
        color = MaterialTheme.colorScheme.primary,
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
private fun ReminderField(
    task: State<Task>,
    onReminderChanged: (Reminder?) -> Unit
) {
    val reminder by remember { derivedStateOf { task.value.reminder } }
    val reminderDate = task.value.reminderDateTime()
    val reminderFormatted = if (task.value.startDate?.dayOfMonth != reminderDate?.dayOfMonth) {
        reminderDate?.formatMediumDateAndTime()
    } else {
        reminderDate?.formatShortTime()
    }

    var isReminderClicked by rememberSaveable { mutableStateOf(false) }
    var isSetReminderAllowed by rememberSaveable { mutableStateOf(false) }
    var isReminderDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (isReminderClicked) {
        with(task.value) {
            if (this.startDate != null && this.startTime != null) {
                isSetReminderAllowed = true
            } else {
                RequiredStartDateAndTimeDialog(
                    onConfirmClicked = {
                        isReminderClicked = false
                    }
                )
            }
        }
    }

    CheckNotificationPermission(
        onShowDialog = { isSetReminderAllowed },
        onResult = { isGranted ->
            isReminderClicked = false
            isSetReminderAllowed = false
            isReminderDialogOpen = isGranted
        }
    )

    if (isReminderDialogOpen) {
        val context = LocalContext.current

        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, ToDoAppBootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        ReminderDialog(
            initialValue = reminder ?: Reminder(0, 0, 1),
            onDismissRequest = { isReminderDialogOpen = false },
            onConfirmClicked = {
                onReminderChanged(it)
                isReminderDialogOpen = false
            }
        )
    }

    Text(
        modifier = Modifier
            .padding(vertical = ToDoAppTheme.spacing.extraSmall),
        text = stringResource(R.string.reminder),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

    SmallTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = reminderFormatted ?: "",
        placeholder = stringResource(R.string.add_reminder),
        onClick = { isReminderClicked = true },
        leadingIcon = {
            ToDoAppIcon(
                icon = ToDoAppIcons.icReminder,
                contentDescription = "reminder"
            )
        },
        trailingIcon = {
            reminder?.let {
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icRemove,
                    onClick = { onReminderChanged(null) }
                )
            }
        }
    )
}

@Composable
fun RequiredStartDateAndTimeDialog(
    onConfirmClicked: () -> Unit
) {
    var isRequiredStartTimeDialogOpen by remember { mutableStateOf(true) }

    if (isRequiredStartTimeDialogOpen) {
        DefaultDialog(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.large),
            onDismissRequest = { isRequiredStartTimeDialogOpen = false }
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = ToDoAppTheme.spacing.large,
                        top = ToDoAppTheme.spacing.large,
                        end = ToDoAppTheme.spacing.large,
                        bottom = ToDoAppTheme.spacing.medium
                    )
            ) {
                Text(
                    text = stringResource(R.string.attention),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = ToDoAppTheme.spacing.large),
                    text = stringResource(R.string.set_reminder_require_start_dates),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                SmallButton(
                    modifier = Modifier
                        .padding(top = ToDoAppTheme.spacing.medium)
                        .align(Alignment.End),
                    onClick = {
                        isRequiredStartTimeDialogOpen = false
                        onConfirmClicked()
                    },
                    text = stringResource(R.string.ok)
                )
            }
        }
    }
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
            wasDatesExplanationClosed = remember { mutableStateOf(true) },
            onWasDatesExplanationClosed = { },
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
            onReminderChanged = {},
            navigateToSelectLocation = {}
        )
    }
}
