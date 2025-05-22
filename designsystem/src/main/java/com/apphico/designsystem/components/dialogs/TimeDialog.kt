package com.apphico.designsystem.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.getLocalTime
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    isTimePickerDialogOpen: MutableState<Boolean>,
    timePickerState: TimePickerState,
    onTimeChanged: (LocalTime?) -> Unit
) {
    var isKeyboardMode by remember { mutableStateOf(false) }

    if (isTimePickerDialogOpen.value) {
        DefaultDialog(
            onDismissRequest = {
                isTimePickerDialogOpen.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = ToDoAppTheme.spacing.large)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (isKeyboardMode) TimeInput(state = timePickerState) else TimePicker(state = timePickerState)
                }
                Row {
                    ToDoAppIconButton(
                        modifier = Modifier
                            .padding(start = ToDoAppTheme.spacing.small),
                        icon = if (isKeyboardMode) ToDoAppIcons.icClock else ToDoAppIcons.icKeyboard,
                        onClick = {
                            isKeyboardMode = !isKeyboardMode
                        }
                    )
                    Row(
                        modifier = Modifier
                            .offset(x = (-8).dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        NormalButton(
                            modifier = Modifier
                                .padding(end = ToDoAppTheme.spacing.small),
                            onClick = {
                                onTimeChanged(null)
                                isTimePickerDialogOpen.value = false
                            },
                            text = stringResource(R.string.remove)
                        )
                        NormalButton(
                            modifier = Modifier
                                .padding(end = ToDoAppTheme.spacing.medium),
                            onClick = {
                                onTimeChanged(timePickerState.getLocalTime())
                                isTimePickerDialogOpen.value = false
                            },
                            text = stringResource(R.string.ok)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun TimeDialogPreview() {
    ToDoAppTheme {
        TimeDialog(
            isTimePickerDialogOpen = remember { mutableStateOf(true) },
            timePickerState = rememberTimePickerState(),
            onTimeChanged = {}
        )
    }
}