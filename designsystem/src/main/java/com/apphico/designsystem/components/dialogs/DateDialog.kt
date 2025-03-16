package com.apphico.designsystem.components.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getLocalDate
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateDialog(
    isDatePickerDialogOpen: MutableState<Boolean>,
    datePickerState: DatePickerState,
    onDateChanged: (LocalDate?) -> Unit
) {
    DatePickerDialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
        onDismissRequest = {
            isDatePickerDialogOpen.value = false
        },
        dismissButton = {
            NormalButton(
                onClick = {
                    isDatePickerDialogOpen.value = false
                },
                text = stringResource(R.string.cancel)
            )
        },
        confirmButton = {
            NormalButton(
                modifier = Modifier
                    .padding(end = ToDoAppTheme.spacing.small),
                onClick = {
                    onDateChanged(datePickerState.selectedDateMillis?.getLocalDate())
                    isDatePickerDialogOpen.value = false
                },
                text = stringResource(R.string.ok)
            )
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DateDialogPreview(
) {
    ToDoAppTheme {
        DateDialog(
            isDatePickerDialogOpen = remember { mutableStateOf(true) },
            datePickerState = rememberDatePickerState(),
            onDateChanged = {}
        )
    }
}
