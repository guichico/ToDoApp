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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.designsystem.R
import com.apphico.designsystem.components.dialogs.DateDialog
import com.apphico.designsystem.components.dialogs.TimeDialog
import com.apphico.designsystem.components.dialogs.showDiscardChangesDialogOnBackIfNeed
import com.apphico.designsystem.components.textfield.DecimalTextField
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.components.topbar.DeleteSaveTopBar
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun AddEditProgressScreen(
    addProgressViewModel: AddEditProgressViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val isEditing = addProgressViewModel.isEditing

    val showDiscardChangesDialogOnBackIfNeed = showDiscardChangesDialogOnBackIfNeed(
        hasChanges = addProgressViewModel::hasChanges,
        navigateBack = navigateBack
    )

    DeleteSaveTopBar(
        title = stringResource(R.string.add_progress),
        isEditing = isEditing,
        onSaveClicked = {},
        onDeleteClicked = {},
        navigateBack = {
            showDiscardChangesDialogOnBackIfNeed()
        }
    ) { innerPadding ->
        AddEditProgressScreenContent(
            innerPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProgressScreenContent(
    innerPadding: PaddingValues
) {
    val datePickerState = rememberDatePickerState(
        // initialSelectedDateMillis = startDate.value?.toMillis() ?: getGMTNowMillis()
    )
    val timePickerState = rememberTimePickerState(
        /*initialHour = startTime.value?.hour ?: 0,
        initialMinute = startTime.value?.minute ?: 0*/
    )

    val isDatePickerDialogOpen = remember { mutableStateOf(false) }
    val isTimePickerDialogOpen = remember { mutableStateOf(false) }

    if (isDatePickerDialogOpen.value) {
        DateDialog(
            isDatePickerDialogOpen = isDatePickerDialogOpen,
            datePickerState = datePickerState,
            onDateChanged = { }
        )
    }

    if (isTimePickerDialogOpen.value) {
        TimeDialog(
            isTimePickerDialogOpen = isTimePickerDialogOpen,
            timePickerState = timePickerState,
            onTimeChanged = { }
        )
    }

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
            DecimalTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                initialValue = 0f,
                placeholder = stringResource(R.string.progress),
                onValueChange = {
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))
            Row {
                NormalTextField(
                    modifier = Modifier
                        .weight(0.6f),
                    value = "",
                    placeholder = stringResource(R.string.date),
                    onClick = { isDatePickerDialogOpen.value = true }
                )
                Spacer(modifier = Modifier.weight(0.02f))
                NormalTextField(
                    modifier = Modifier
                        .weight(0.4f),
                    value = "",
                    placeholder = stringResource(R.string.hour),
                    onClick = { isTimePickerDialogOpen.value = true }
                )
            }
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))
            NormalTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "",
                placeholder = stringResource(id = R.string.description),
                onValueChange = { },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AddEditProgressPreview() {
    ToDoAppTheme {
        AddEditProgressScreenContent(
            innerPadding = PaddingValues()
        )
    }
}