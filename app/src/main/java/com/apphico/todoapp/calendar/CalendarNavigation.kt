package com.apphico.todoapp.calendar

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Task
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
object CalendarRoute

fun NavGraphBuilder.calendarScreen(
    calendarViewMode: State<CalendarViewMode>,
    selectedDate: State<LocalDate>,
    onTaskClicked: (Task?) -> Unit
) {
    composable<CalendarRoute> {
        CalendarScreen(
            calendarViewMode = calendarViewMode,
            selectedDate = selectedDate,
            navigateToAddEditTask = onTaskClicked
        )
    }
}