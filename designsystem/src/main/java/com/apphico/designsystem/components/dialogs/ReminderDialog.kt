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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.components.picker.NumberPicker
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ReminderDialog(
    date: State<LocalDate?>,
    time: State<LocalTime?>,
    onDismissRequest: () -> Unit,
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

            var minutes by remember { mutableIntStateOf(0) }
            var hours by remember { mutableIntStateOf(0) }
            var days by remember { mutableIntStateOf(0) }

            NumberSpinnerField(
                label = stringResource(R.string.minute),
                items = remember { (0..59).map { it.toString() } },
                onSelectedItemChanged = { minutes = it.toIntOrNull() ?: 0 }
            )
            NumberSpinnerField(
                label = stringResource(R.string.hour),
                items = remember { (0..23).map { it.toString() } },
                onSelectedItemChanged = { hours = it.toIntOrNull() ?: 0 }
            )
            NumberSpinnerField(
                label = stringResource(R.string.day),
                items = remember { (0..99).map { it.toString() } },
                onSelectedItemChanged = { days = it.toIntOrNull() ?: 0 }
            )

            val timeBeforeTask = StringBuilder()
                .apply {
                    val dateSeparator = stringResource(R.string.date_separator)

                    if (days > 0) append("$days ${stringResource(if (days > 1) R.string.days else R.string.day).lowercase()}")
                    if (days > 0 && hours > 0 && minutes > 0) append(", ") else if (days > 0 && hours > 0) append("$dateSeparator ") else append(" ")
                    if (hours > 0) append("$hours ${stringResource(if (hours > 1) R.string.hours else R.string.hour).lowercase()} ")
                    if ((days > 0 || hours > 0) && minutes > 0) append("$dateSeparator ")
                    if (minutes > 0) append("$minutes ${stringResource(if (minutes > 1) R.string.minutes else R.string.minute).lowercase()} ")

                    append(stringResource(R.string.before_task)).toString()
                }
                .toString()

            Text(
                modifier = Modifier
                    .padding(vertical = ToDoAppTheme.spacing.large),
                text = timeBeforeTask,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                NormalButton(
                    modifier = Modifier
                        .padding(end = ToDoAppTheme.spacing.small),
                    onClick = {

                    },
                    text = stringResource(R.string.cancel)
                )
                NormalButton(
                    onClick = {

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
    onSelectedItemChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ToDoAppTheme.spacing.small),
        horizontalArrangement = Arrangement.Center
    ) {
        NumberPicker(
            modifier = Modifier
                .weight(0.65f),
            items = items,
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
            date = remember { mutableStateOf(getNowDate()) },
            time = remember { mutableStateOf(getNowTime()) },
            onDismissRequest = {}
        )
    }
}