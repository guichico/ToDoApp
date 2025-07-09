package com.apphico.todoapp.achievements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.core_model.Progress
import com.apphico.designsystem.R
import com.apphico.designsystem.components.dialogs.DateDialog
import com.apphico.designsystem.components.dialogs.TimeDialog
import com.apphico.designsystem.components.dialogs.showDiscardChangesDialogOnBackIfNeed
import com.apphico.designsystem.components.textfield.CurrencyTextField
import com.apphico.designsystem.components.textfield.DecimalTextField
import com.apphico.designsystem.components.textfield.IntTextField
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.textfield.PercentageTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.formatMediumDate
import com.apphico.extensions.formatShortTime
import com.apphico.extensions.getNowGMTMillis
import com.apphico.extensions.toMillis
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddEditProgressScreen(
    addProgressViewModel: AddEditProgressViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    onProgressChanged: (Int, Operation) -> Unit
) {
    val editingProgress = addProgressViewModel.editingProgress.collectAsState()
    val progressError = addProgressViewModel.progressError.collectAsState()

    val measurementType = addProgressViewModel.measurementType
    val measurementUnit = addProgressViewModel.measurementUnit

    val isEditing = addProgressViewModel.isEditing

    val showDiscardChangesDialogOnBackIfNeed = showDiscardChangesDialogOnBackIfNeed(
        hasChanges = addProgressViewModel::hasChanges,
        navigateBack = navigateBack
    )

    DeleteSaveTopBar(
        title = stringResource(R.string.add_progress),
        isEditing = isEditing,
        onSaveClicked = {
            addProgressViewModel.save { operation ->
                onProgressChanged(measurementType, operation)
            }
        },
        onDeleteClicked = {
            addProgressViewModel.delete { operation ->
                onProgressChanged(measurementType, operation)
            }
        },
        navigateBack = {
            showDiscardChangesDialogOnBackIfNeed()
        }
    ) { innerPadding ->
        AddEditProgressScreenContent(
            innerPadding = innerPadding,
            progress = editingProgress,
            measurementTypeInt = measurementType,
            measurementUnit = measurementUnit,
            progressError = progressError,
            onProgressChanged = addProgressViewModel::onProgressChanged,
            onDescriptionChanged = addProgressViewModel::onDescriptionChanged,
            onDateChanged = addProgressViewModel::onDateChanged,
            onTimeChanged = addProgressViewModel::onTimeChanged
        )
    }
}

@Composable
fun AddEditProgressScreenContent(
    innerPadding: PaddingValues,
    progress: State<Progress>,
    measurementTypeInt: Int,
    measurementUnit: MeasurementValueUnit?,
    progressError: State<Int?>,
    onProgressChanged: (Float) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDateChanged: (LocalDate?) -> Unit,
    onTimeChanged: (LocalTime?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
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
            ProgressTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                measurementTypeInt = measurementTypeInt,
                measurementUnit = measurementUnit,
                progress = progress,
                progressError = progressError,
                onProgressChanged = onProgressChanged
            )
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))
            NormalTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = progress.value.description ?: "",
                placeholder = stringResource(id = R.string.description),
                onValueChange = onDescriptionChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))
            DateRow(
                date = remember { derivedStateOf { progress.value.date } },
                time = remember { derivedStateOf { progress.value.time } },
                onDateChanged = onDateChanged,
                onTimeChanged = onTimeChanged
            )
        }
    }
}

@Composable
private fun ProgressTextField(
    modifier: Modifier = Modifier,
    measurementTypeInt: Int,
    measurementUnit: MeasurementValueUnit?,
    progress: State<Progress>,
    progressError: State<Int?>,
    onProgressChanged: (Float) -> Unit
) {
    if (measurementTypeInt == MeasurementType.Percentage().intValue) {
        PercentageTextField(
            modifier = modifier,
            initialValue = progress.value.progress,
            placeholder = stringResource(R.string.progress),
            isError = progressError.value != null,
            errorMessage = progressError.value?.let { stringResource(it) } ?: "",
            onValueChange = onProgressChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
    } else {
        when (measurementUnit) {
            MeasurementValueUnit.DECIMAL -> {
                DecimalTextField(
                    modifier = modifier,
                    initialValue = progress.value.progress,
                    placeholder = stringResource(R.string.progress),
                    isError = progressError.value != null,
                    errorMessage = progressError.value?.let { stringResource(it) } ?: "",
                    onValueChange = onProgressChanged,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            MeasurementValueUnit.CURRENCY -> {
                CurrencyTextField(
                    modifier = modifier,
                    initialValue = progress.value.progress,
                    placeholder = stringResource(R.string.progress),
                    isError = progressError.value != null,
                    errorMessage = progressError.value?.let { stringResource(it) } ?: "",
                    onValueChange = onProgressChanged,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            else -> {
                IntTextField(
                    modifier = modifier,
                    initialValue = progress.value.progress,
                    placeholder = stringResource(R.string.progress),
                    isError = progressError.value != null,
                    errorMessage = progressError.value?.let { stringResource(it) } ?: "",
                    onValueChange = onProgressChanged,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRow(
    date: State<LocalDate?>,
    time: State<LocalTime?>,
    onDateChanged: (LocalDate?) -> Unit,
    onTimeChanged: (LocalTime?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.value?.toMillis() ?: getNowGMTMillis()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = time.value?.hour ?: 0,
        initialMinute = time.value?.minute ?: 0
    )

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

    Row {
        NormalTextField(
            modifier = Modifier
                .weight(0.6f),
            value = date.value?.formatMediumDate() ?: "",
            placeholder = stringResource(R.string.date),
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
}

@PreviewLightDark
@Composable
private fun AddEditProgressPreview() {
    ToDoAppTheme {
        AddEditProgressScreenContent(
            innerPadding = PaddingValues(),
            progress = remember { mutableStateOf(Progress()) },
            measurementTypeInt = MeasurementType.Percentage().intValue,
            measurementUnit = MeasurementValueUnit.DECIMAL,
            progressError = remember { mutableStateOf(null) },
            onProgressChanged = {},
            onDescriptionChanged = {},
            onDateChanged = {},
            onTimeChanged = {}
        )
    }
}
