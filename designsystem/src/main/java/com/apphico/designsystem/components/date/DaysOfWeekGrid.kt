package com.apphico.designsystem.components.date

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import java.text.DateFormatSymbols
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DaysOfWeekGrid(
    selectedDaysState: State<List<Int>>,
    onSelectionChanged: (List<Int>) -> Unit
) {
    // TODO Check locatization
    val daysOfWeek = DateFormatSymbols.getInstance()
        .shortWeekdays.filterNot { it.isNullOrEmpty() }.map { it.first().toString().uppercase(Locale.getDefault()) }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.medium),
        maxItemsInEachRow = 7
    ) {
        daysOfWeek.forEachIndexed { index, dayOfWeek ->
            val isSelected = selectedDaysState.value.contains(index + 1)
            val backColor = if (isSelected) MaterialTheme.colorScheme.secondary else White
            val shape = CircleShape

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = ToDoAppTheme.spacing.extraSmall)
                    .aspectRatio(1f)
                    .shadow(2.dp, shape, true)
                    .background(backColor, shape)
                    .border(
                        shape = shape,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                    .clickable {
                        onSelectionChanged(
                            selectedDaysState
                                .value
                                .toMutableList()
                                .apply { if (!isSelected) add(index + 1) else remove(index + 1) }
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = dayOfWeek,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) White else MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DaysOfWeekGridPreview() {
    ToDoAppTheme {
        DaysOfWeekGrid(
            selectedDaysState = remember { mutableStateOf(listOf(2, 4, 6)) },
            onSelectionChanged = {}
        )
    }
}
