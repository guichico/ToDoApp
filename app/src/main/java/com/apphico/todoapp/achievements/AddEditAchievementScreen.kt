package com.apphico.todoapp.achievements

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.core_model.fakeData.mockedAchievements
import com.apphico.designsystem.R
import com.apphico.designsystem.animatedElevation
import com.apphico.designsystem.components.card.AddEditHeader
import com.apphico.designsystem.components.card.ProgressCard
import com.apphico.designsystem.components.checklist.CreateCheckList
import com.apphico.designsystem.components.dialogs.CheckBoxDialog
import com.apphico.designsystem.components.dialogs.DateDialog
import com.apphico.designsystem.components.dialogs.DefaultDialog
import com.apphico.designsystem.components.dialogs.showDiscardChangesDialogOnBackIfNeed
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.textfield.CurrencyTextField
import com.apphico.designsystem.components.textfield.DecimalTextField
import com.apphico.designsystem.components.textfield.IntTextField
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.textfield.SmallTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.format
import com.apphico.extensions.formatMediumDate
import com.apphico.extensions.getNowGMTMillis
import java.time.LocalDate

private val MeasurementValueUnit.label: Int
    @StringRes get() = when (this) {
        MeasurementValueUnit.INT -> R.string.unit_int
        MeasurementValueUnit.DECIMAL -> R.string.unit_decimal
        MeasurementValueUnit.CURRENCY -> R.string.unit_currency
    }

@Composable
fun AddEditAchievementScreen(
    addEditAchievementViewModel: AddEditAchievementViewModel = hiltViewModel(),
    snackBar: (String) -> Unit,
    navigateToSelectGroup: () -> Unit,
    navigateToAddEditProgress: () -> Unit,
    navigateBack: () -> Unit
) {
    val editingAchievement = addEditAchievementViewModel.editingAchievement.collectAsState()
    val editingMeasurementType = addEditAchievementViewModel.editingMeasurementType.collectAsState()
    val editingCheckList = addEditAchievementViewModel.editingCheckList.collectAsState()
    val editingPercentageProgress = addEditAchievementViewModel.editingPercentageProgress.collectAsState()
    val progress = addEditAchievementViewModel.progress.collectAsState()

    val isEditing = addEditAchievementViewModel.isEditing

    val nameError = addEditAchievementViewModel.nameError.collectAsState()

    val achievementSaveSuccess = stringResource(R.string.achievement_saved)
    val achievementSaveError = stringResource(R.string.achievement_save_error)

    val achievementDeleteSuccess = stringResource(R.string.achievement_deleted)
    val achievementDeleteError = stringResource(R.string.achievement_delete_error)

    val showDiscardChangesDialogOnBackIfNeed = showDiscardChangesDialogOnBackIfNeed(
        hasChanges = addEditAchievementViewModel::hasChanges,
        navigateBack = navigateBack
    )

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
        title = stringResource(R.string.add_new_achievement),
        isEditing = isEditing,
        onSaveClicked = {
            addEditAchievementViewModel.save { isSuccess ->
                snackBar(if (isSuccess) achievementSaveSuccess else achievementSaveError)
                if (isSuccess) navigateBack()
            }
        },
        onDeleteClicked = {
            addEditAchievementViewModel.delete { isSuccess ->
                snackBar(if (isSuccess) achievementDeleteSuccess else achievementDeleteError)
                if (isSuccess) navigateBack()
            }
        },
        navigateBack = {
            showDiscardChangesDialogOnBackIfNeed()
        }
    ) { innerPadding ->
        AddEditAchievementScreenContent(
            innerPadding = innerPadding,
            scrollState = scrollState,
            achievement = editingAchievement,
            progress = progress,
            measurementType = editingMeasurementType,
            checkList = editingCheckList,
            percentageProgress = editingPercentageProgress,
            isEditing = isEditing,
            onNameChange = addEditAchievementViewModel::onNameChanged,
            nameError = nameError,
            onDescriptionChange = addEditAchievementViewModel::onDescriptionChanged,
            navigateToSelectGroup = navigateToSelectGroup,
            onGroupRemoved = addEditAchievementViewModel::onGroupRemoved,
            onEndDateChanged = addEditAchievementViewModel::onEndDateChanged,
            onMeasurementTypeChanged = addEditAchievementViewModel::onMeasurementTypeChanged,
            onCheckListItemChanged = addEditAchievementViewModel::onCheckListItemChanged,
            onCheckListItemItemAdded = addEditAchievementViewModel::onCheckListItemItemAdded,
            onCheckListItemItemRemoved = addEditAchievementViewModel::onCheckListItemItemRemoved,
            onCheckListItemDoneChanged = addEditAchievementViewModel::setCheckListItemDone,
            onUnitChanged = addEditAchievementViewModel::onUnitChanged,
            onStartingValueChanged = addEditAchievementViewModel::ondStartingValueChanged,
            onGoalValueChanged = addEditAchievementViewModel::ondGoalValueChanged,
            onTrackedValuesChanged = addEditAchievementViewModel::onTrackedValuesChanged,
            navigateToAddEditProgress = navigateToAddEditProgress
        )
    }
}

@Composable
private fun AddEditAchievementScreenContent(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    achievement: State<Achievement>,
    progress: State<Float>,
    measurementType: State<MeasurementType?>,
    isEditing: Boolean,
    nameError: State<Int?>,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    navigateToSelectGroup: () -> Unit,
    onGroupRemoved: () -> Unit,
    onEndDateChanged: (LocalDate?) -> Unit,
    onMeasurementTypeChanged: (MeasurementType) -> Unit,
    percentageProgress: State<List<MeasurementType.Percentage.PercentageProgress>>,
    checkList: State<List<CheckListItem>>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,
    onUnitChanged: (MeasurementValueUnit) -> Unit,
    onStartingValueChanged: (Float) -> Unit,
    onGoalValueChanged: (Float) -> Unit,
    onTrackedValuesChanged: (List<MeasurementType.Value.TrackedValues>) -> Unit,
    navigateToAddEditProgress: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
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
                    nameValue = remember { derivedStateOf { achievement.value.name } },
                    namePlaceholder = stringResource(id = R.string.title),
                    nameError = nameError,
                    onNameChange = onNameChange,
                    descriptionValue = remember { derivedStateOf { achievement.value.description } },
                    descriptionPlaceholder = stringResource(id = R.string.description),
                    onDescriptionChange = onDescriptionChange,
                    group = remember { derivedStateOf { achievement.value.group } },
                    groupPlaceholder = stringResource(id = R.string.select_group),
                    navigateToSelectGroup = navigateToSelectGroup,
                    onGroupRemoved = onGroupRemoved
                )

                Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

                EndDateField(
                    endDate = remember { derivedStateOf { achievement.value.endDate } },
                    onEndDateChanged = onEndDateChanged
                )

                MeasurementTypeFields(
                    measurementType = measurementType,
                    progress = progress,
                    scrollState = scrollState,
                    onMeasurementTypeChanged = onMeasurementTypeChanged,
                    parentDate = remember { derivedStateOf { achievement.value.endDate } },
                    percentageProgress = percentageProgress,
                    checkList = checkList,
                    onCheckListItemChanged = onCheckListItemChanged,
                    onCheckListItemItemAdded = onCheckListItemItemAdded,
                    onCheckListItemItemRemoved = onCheckListItemItemRemoved,
                    onCheckListItemDoneChanged = onCheckListItemDoneChanged,
                    onUnitChanged = onUnitChanged,
                    onStartingValueChanged = onStartingValueChanged,
                    onGoalValueChanged = onGoalValueChanged,
                    onTrackedValuesChanged = onTrackedValuesChanged,
                    navigateToAddEditProgress = navigateToAddEditProgress
                )
            }
        }
        DoneButton(
            isEditing = isEditing,
            progress = progress
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EndDateField(
    endDate: State<LocalDate?>,
    onEndDateChanged: (LocalDate?) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val isDatePickerDialogOpen = remember { mutableStateOf(false) }

    if (isDatePickerDialogOpen.value) {
        DateDialog(
            isDatePickerDialogOpen = isDatePickerDialogOpen,
            datePickerState = datePickerState,
            onDateChanged = onEndDateChanged
        )
    }

    NormalTextField(
        modifier = Modifier.fillMaxWidth(),
        value = endDate.value?.formatMediumDate() ?: "",
        placeholder = stringResource(R.string.end_date),
        onClick = {
            if (datePickerState.selectedDateMillis == null)
                datePickerState.selectedDateMillis = getNowGMTMillis()
            isDatePickerDialogOpen.value = true
        }
    )
}

@Composable
private fun MeasurementTypeFields(
    measurementType: State<MeasurementType?>,
    progress: State<Float>,
    scrollState: ScrollState,
    onMeasurementTypeChanged: (MeasurementType) -> Unit,
    parentDate: State<LocalDate?>,
    percentageProgress: State<List<MeasurementType.Percentage.PercentageProgress>>,
    checkList: State<List<CheckListItem>>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,
    onUnitChanged: (MeasurementValueUnit) -> Unit,
    onStartingValueChanged: (Float) -> Unit,
    onGoalValueChanged: (Float) -> Unit,
    onTrackedValuesChanged: (List<MeasurementType.Value.TrackedValues>) -> Unit,
    navigateToAddEditProgress: () -> Unit
) {
    val measurementUnit = remember {
        derivedStateOf {
            if (measurementType.value is MeasurementType.Value) {
                (measurementType.value as MeasurementType.Value).unit
            } else {
                null
            }
        }
    }

    if (progress.value == 0f) {
        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))
        MeasurementTypeDialog(
            measurementType = measurementType,
            onMeasurementTypeChanged = onMeasurementTypeChanged
        )
    }

    Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

    AnimatedContent(
        targetState = measurementType.value,
        label = ""
    ) {
        MeasurementTypeView(
            scrollState = scrollState,
            totalProgress = progress,
            measurementType = remember { derivedStateOf { it } },
            measurementUnit = measurementUnit,
            parentDate = parentDate,
            percentageProgress = percentageProgress,
            checkList = checkList,
            onCheckListItemChanged = onCheckListItemChanged,
            onCheckListItemItemAdded = onCheckListItemItemAdded,
            onCheckListItemItemRemoved = onCheckListItemItemRemoved,
            onCheckListItemDoneChanged = onCheckListItemDoneChanged,
            onUnitChanged = onUnitChanged,
            onStartingValueChanged = onStartingValueChanged,
            onGoalValueChanged = onGoalValueChanged,
            onTrackedValuesChanged = onTrackedValuesChanged,
            navigateToAddEditProgress = navigateToAddEditProgress
        )
    }
}

@Composable
private fun MeasurementTypeView(
    scrollState: ScrollState,
    totalProgress: State<Float>,
    measurementType: State<MeasurementType?>,
    measurementUnit: State<MeasurementValueUnit?>,
    parentDate: State<LocalDate?>,
    percentageProgress: State<List<MeasurementType.Percentage.PercentageProgress>>,
    checkList: State<List<CheckListItem>>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,
    onUnitChanged: (MeasurementValueUnit) -> Unit,
    onStartingValueChanged: (Float) -> Unit,
    onGoalValueChanged: (Float) -> Unit,
    onTrackedValuesChanged: (List<MeasurementType.Value.TrackedValues>) -> Unit,
    navigateToAddEditProgress: () -> Unit
) {
    when (measurementType.value) {
        is MeasurementType.TaskDone -> {
            MeasurementTypeCheckList(
                scrollState = scrollState,
                parentDate = parentDate,
                checkList = checkList,
                onCheckListItemChanged = onCheckListItemChanged,
                onCheckListItemItemAdded = onCheckListItemItemAdded,
                onCheckListItemItemRemoved = onCheckListItemItemRemoved,
                onCheckListItemDoneChanged = onCheckListItemDoneChanged
            )
        }

        is MeasurementType.Percentage -> {
            MeasurementTypePercentage(
                percentageProgress = percentageProgress,
                navigateToAddEditProgress = navigateToAddEditProgress
            )
        }

        is MeasurementType.Value -> {
            MeasurementTypeValue(
                totalProgress = totalProgress,
                onUnitChanged = onUnitChanged,
                valueProgress = remember { derivedStateOf { measurementType.value as MeasurementType.Value } },
                measurementUnit = measurementUnit,
                onStartingValueChanged = onStartingValueChanged,
                onGoalValueChanged = onGoalValueChanged,
                onTrackedValuesChanged = onTrackedValuesChanged,
                navigateToAddEditProgress = navigateToAddEditProgress
            )
        }

        is MeasurementType.None,
        null -> {
        }
    }
}

@Composable
private fun MeasurementTypeDialog(
    measurementType: State<MeasurementType?>,
    onMeasurementTypeChanged: (MeasurementType) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    val type = measurementType.value

    NormalTextField(
        modifier = Modifier.fillMaxWidth(),
        value = type?.title?.let { stringResource(it) } ?: "",
        placeholder = stringResource(R.string.achievement_goal_type),
        onClick = { isDialogOpen = true },
        trailingIcon = {
            ToDoAppIcon(
                icon = ToDoAppIcons.icArrowRight,
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }
    )

    if (isDialogOpen) {
        CheckBoxDialog(
            values = listOf(
                MeasurementType.TaskDone(),
                MeasurementType.Percentage(),
                MeasurementType.Value(startingValue = 0f, goalValue = 0f),
                MeasurementType.None
            ),
            selectedItem = measurementType.value,
            onItemSelected = {
                onMeasurementTypeChanged(it as MeasurementType)
                isDialogOpen = false
            },
            onDismissRequest = { isDialogOpen = false }
        )
    }
}

@Composable
private fun MeasurementTypeCheckList(
    scrollState: ScrollState,
    parentDate: State<LocalDate?>,
    checkList: State<List<CheckListItem>>,
    onCheckListItemChanged: (CheckListItem, CheckListItem) -> Unit,
    onCheckListItemItemAdded: (CheckListItem) -> Unit,
    onCheckListItemItemRemoved: (CheckListItem) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit
) {
    Column {
        Text(
            modifier = Modifier,
            text = stringResource(R.string.checklist_header_title),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.medium))
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
}

@Composable
private fun MeasurementTypePercentage(
    percentageProgress: State<List<MeasurementType.Percentage.PercentageProgress>>,
    navigateToAddEditProgress: () -> Unit
) {
    val totalProgress = percentageProgress.value.lastOrNull()?.progress ?: 0f

    Column {
        Text(
            modifier = Modifier
                .padding(vertical = ToDoAppTheme.spacing.extraSmall),
            text = stringResource(R.string.progress),
            style = MaterialTheme.typography.bodyMedium
        )
        percentageProgress.value.forEach {
            ProgressCard(
                date = it.date,
                time = it.time,
                description = it.description,
                progress = it.progress,
                onClick = navigateToAddEditProgress
            )
        }
        if (totalProgress < 1f) {
            SmallTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ToDoAppTheme.spacing.small),
                value = stringResource(R.string.add_progress),
                onClick = navigateToAddEditProgress,
                trailingIcon = {
                    ToDoAppIcon(
                        icon = ToDoAppIcons.icAdd,
                        contentDescription = "add"
                    )
                }
            )
        }
    }
}

@Composable
private fun MeasurementTypeValue(
    totalProgress: State<Float>,
    onUnitChanged: (MeasurementValueUnit) -> Unit,
    valueProgress: State<MeasurementType.Value?>,
    measurementUnit: State<MeasurementValueUnit?>,
    onStartingValueChanged: (Float) -> Unit,
    onGoalValueChanged: (Float) -> Unit,
    onTrackedValuesChanged: (List<MeasurementType.Value.TrackedValues>) -> Unit,
    navigateToAddEditProgress: () -> Unit
) {
    val focusRequester = FocusRequester()

    Column {
        Text(
            modifier = Modifier
                .padding(vertical = ToDoAppTheme.spacing.extraSmall),
            text = stringResource(R.string.goal_value),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

        UnitDialog(
            measurementUnit = measurementUnit,
            onUnitChanged = onUnitChanged
        )

        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

        StartingValueField(
            measurementUnit = measurementUnit,
            valueProgress = valueProgress,
            onStartingValueChanged = onStartingValueChanged,
            focusRequester = focusRequester
        )

        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))

        GoalValueField(
            measurementUnit = measurementUnit,
            valueProgress = valueProgress,
            onGoalValueChanged = onGoalValueChanged,
            focusRequester = focusRequester
        )

        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

        Text(
            modifier = Modifier
                .padding(vertical = ToDoAppTheme.spacing.extraSmall),
            text = stringResource(R.string.progress),
            style = MaterialTheme.typography.bodyMedium
        )

        valueProgress.value?.let { vp ->
            vp.trackedValues.forEach {
                val track = vp.goalValue - vp.startingValue
                val progress = (vp.startingValue - it.trackedValue) / track

                ProgressCard(
                    date = it.date.toLocalDate(),
                    time = it.date.toLocalTime(),
                    progressText = "${it.trackedValue.format()}/${vp.goalValue.format()}",
                    description = it.description,
                    progress = if (progress < 0) progress * -1 else progress,
                    onClick = navigateToAddEditProgress
                )
            }
        }
        if (totalProgress.value < 1f) {
            SmallTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ToDoAppTheme.spacing.small),
                value = stringResource(R.string.add_progress),
                onClick = navigateToAddEditProgress,
                trailingIcon = {
                    ToDoAppIcon(
                        icon = ToDoAppIcons.icAdd,
                        contentDescription = "add"
                    )
                }
            )
        }
    }
}

@Composable
private fun UnitDialog(
    measurementUnit: State<MeasurementValueUnit?>,
    onUnitChanged: (MeasurementValueUnit) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    NormalTextField(
        modifier = Modifier.fillMaxWidth(),
        value = measurementUnit.value?.label?.let { stringResource(it) } ?: "",
        placeholder = stringResource(R.string.achievement_unit),
        onClick = { isDialogOpen = true },
        trailingIcon = {
            ToDoAppIcon(
                icon = ToDoAppIcons.icArrowRight,
                contentDescription = "arrow",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }
    )

    if (isDialogOpen) {
        DefaultDialog(
            onDismissRequest = { isDialogOpen = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = ToDoAppTheme.spacing.medium
                    )
            ) {
                MeasurementValueUnit.entries.forEach { unit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUnitChanged(unit)
                                isDialogOpen = false
                            }
                    ) {
                        ToDoAppIcon(
                            modifier = Modifier
                                .padding(
                                    start = ToDoAppTheme.spacing.large,
                                    top = ToDoAppTheme.spacing.large,
                                    bottom = ToDoAppTheme.spacing.large
                                )
                                .align(Alignment.CenterVertically),
                            icon = if (measurementUnit.value == unit) ToDoAppIcons.icRadioButtonChecked else ToDoAppIcons.icRadioButtonUnchecked,
                            contentDescription = "select",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier
                                .padding(ToDoAppTheme.spacing.large)
                                .align(Alignment.CenterVertically),
                            text = stringResource(unit.label),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartingValueField(
    measurementUnit: State<MeasurementValueUnit?>,
    valueProgress: State<MeasurementType.Value?>,
    onStartingValueChanged: (Float) -> Unit,
    focusRequester: FocusRequester
) {
    when (measurementUnit.value) {
        MeasurementValueUnit.DECIMAL -> {
            DecimalTextField(
                modifier = Modifier.fillMaxWidth(),
                initialValue = valueProgress.value?.startingValue,
                placeholder = stringResource(R.string.initial_value),
                onValueChange = onStartingValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
            )
        }

        MeasurementValueUnit.CURRENCY -> {
            CurrencyTextField(
                modifier = Modifier.fillMaxWidth(),
                initialValue = valueProgress.value?.startingValue,
                placeholder = stringResource(R.string.initial_value),
                onValueChange = onStartingValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
            )
        }

        else -> {
            IntTextField(
                modifier = Modifier.fillMaxWidth(),
                initialValue = valueProgress.value?.startingValue,
                placeholder = stringResource(R.string.initial_value),
                onValueChange = onStartingValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
            )
        }
    }
}

@Composable
private fun GoalValueField(
    measurementUnit: State<MeasurementValueUnit?>,
    valueProgress: State<MeasurementType.Value?>,
    onGoalValueChanged: (Float) -> Unit,
    focusRequester: FocusRequester
) {
    when (measurementUnit.value) {
        MeasurementValueUnit.DECIMAL -> {
            DecimalTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                initialValue = valueProgress.value?.goalValue,
                placeholder = stringResource(R.string.goal_value),
                onValueChange = onGoalValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        MeasurementValueUnit.CURRENCY -> {
            CurrencyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                initialValue = valueProgress.value?.goalValue,
                placeholder = stringResource(R.string.goal_value),
                onValueChange = onGoalValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        else -> {
            IntTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                initialValue = valueProgress.value?.goalValue,
                placeholder = stringResource(R.string.goal_value),
                onValueChange = onGoalValueChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}

@Composable
private fun DoneButton(
    isEditing: Boolean,
    progress: State<Float>
) {
    if (isEditing && progress.value < 1f) {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RectangleShape,
            onClick = { }
        ) {
            Text(
                modifier = Modifier
                    .padding(ToDoAppTheme.spacing.small),
                text = stringResource(R.string.complete_achievement),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

class AddEditAchievementScreenPreviewProvider : PreviewParameterProvider<Achievement> {
    override val values =
        sequenceOf(
            Achievement(measurementType = MeasurementType.Value(startingValue = 0f, goalValue = 0f)),
            mockedAchievements.first()
        )
}

@PreviewLightDark
@Composable
private fun AddEditAchievementScreenPreview(
    @PreviewParameter(AddEditAchievementScreenPreviewProvider::class) achievement: Achievement
) {
    ToDoAppTheme {
        AddEditAchievementScreenContent(
            innerPadding = PaddingValues(),
            scrollState = ScrollState(0),
            achievement = remember { mutableStateOf(achievement) },
            measurementType = remember { mutableStateOf(MeasurementType.Percentage()) },
            progress = remember { mutableFloatStateOf(achievement.getProgress()) },
            isEditing = true,
            nameError = remember { mutableStateOf(null) },
            onNameChange = {},
            onDescriptionChange = {},
            navigateToSelectGroup = {},
            onGroupRemoved = {},
            onEndDateChanged = {},
            onMeasurementTypeChanged = {},
            percentageProgress = remember { mutableStateOf(emptyList()) },
            checkList = remember { mutableStateOf(emptyList()) },
            onCheckListItemChanged = { _, _ -> },
            onCheckListItemItemAdded = {},
            onCheckListItemItemRemoved = {},
            onCheckListItemDoneChanged = { _, _, _ -> },
            onUnitChanged = {},
            onStartingValueChanged = {},
            onGoalValueChanged = {},
            onTrackedValuesChanged = {},
            navigateToAddEditProgress = {}
        )
    }
}