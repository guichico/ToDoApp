package com.apphico.designsystem.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.core_model.Reminder
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.components.picker.NumberPicker
import com.apphico.designsystem.components.switch.ToDoAppSwitch
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun ReminderDialog(
    initialValue: Reminder,
    onDismissRequest: () -> Unit,
    onConfirmClicked: (Reminder) -> Unit
) {
    DefaultDialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.large)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = ToDoAppTheme.spacing.large),
                text = stringResource(R.string.reminder),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            var minutes by remember { mutableIntStateOf(initialValue.minutes) }
            var hours by remember { mutableIntStateOf(initialValue.hours) }
            var days by remember { mutableIntStateOf(initialValue.days) }

            NumberSpinnerField(
                label = stringResource(R.string.minute),
                items = remember { (0..59).map { it.toString() } },
                startValue = initialValue.minutes.toString(),
                onSelectedItemChanged = { minutes = it.toIntOrNull() ?: 0 }
            )
            NumberSpinnerField(
                label = stringResource(R.string.hour),
                items = remember { (0..23).map { it.toString() } },
                startValue = initialValue.hours.toString(),
                onSelectedItemChanged = { hours = it.toIntOrNull() ?: 0 }
            )
            NumberSpinnerField(
                label = stringResource(R.string.day),
                items = remember { (0..99).map { it.toString() } },
                startValue = initialValue.days.toString(),
                onSelectedItemChanged = { days = it.toIntOrNull() ?: 0 }
            )

            val dateSeparator = stringResource(R.string.date_separator)

            val values = listOf(
                Pair(days, stringResource(if (days > 1) R.string.days else R.string.day).lowercase()),
                Pair(hours, stringResource(if (hours > 1) R.string.hours else R.string.hour).lowercase()),
                Pair(minutes, stringResource(if (minutes > 1) R.string.minutes else R.string.minute).lowercase())
            )
                .filter { (value, _) -> value > 0 }
                .map { (value, text) -> "$value $text" }

            val valuesFormatted = when (values.size) {
                3 -> "${values[0]}, ${values[1]} $dateSeparator ${values[2]} "
                2 -> "${values[0]} $dateSeparator ${values[1]} "
                1 -> "${values[0]} "
                else -> stringResource(R.string.at_task_time)
            }
            val timeBeforeTask = valuesFormatted + if (values.isNotEmpty()) stringResource(R.string.before_task) else ""

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ToDoAppTheme.spacing.large),
                text = timeBeforeTask,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            var isSoundAlarm by remember { mutableStateOf(initialValue.soundAlarm) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = ToDoAppTheme.spacing.extraExtraLarge
                    )
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    text = stringResource(R.string.sound_alarm),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                ToDoAppSwitch(
                    modifier = Modifier
                        .padding(start = ToDoAppTheme.spacing.large),
                    checked = isSoundAlarm,
                    onCheckedChange = { isSoundAlarm = !isSoundAlarm }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                NormalButton(
                    modifier = Modifier
                        .padding(end = ToDoAppTheme.spacing.small),
                    onClick = onDismissRequest,
                    text = stringResource(R.string.cancel)
                )
                NormalButton(
                    onClick = {
                        onConfirmClicked(Reminder(days = days, hours = hours, minutes = minutes, soundAlarm = isSoundAlarm))
                    },
                    text = stringResource(R.string.ok)
                )
            }
        }
    }
}

@Composable
fun NumberSpinnerField(
    label: String,
    items: List<String>,
    startValue: String,
    onSelectedItemChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ToDoAppTheme.spacing.small)
    ) {
        NumberPicker(
            modifier = Modifier
                .weight(0.65f),
            items = items,
            startIndex = items.indexOf(startValue),
            visibleItemsCount = 1,
            onSelectedItemChanged = onSelectedItemChanged
        )
        Spacer(modifier = Modifier.weight(0.05f))
        Box(
            modifier = Modifier
                .weight(0.35f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ReminderDialogPreview() {
    ToDoAppTheme {
        ReminderDialog(
            initialValue = Reminder(days = 0, hours = 2, minutes = 30),
            onDismissRequest = {},
            onConfirmClicked = {},
        )
    }
}