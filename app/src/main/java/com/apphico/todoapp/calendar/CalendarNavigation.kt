package com.apphico.todoapp.calendar

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Task
import kotlinx.serialization.Serializable

@Serializable
object CalendarRoute

fun NavGraphBuilder.calendarScreen(
    calendarViewModel: CalendarViewModel,
    onTaskClicked: (Task?) -> Unit
) {
    composable<CalendarRoute> {
        CalendarScreen(
            calendarViewModel = calendarViewModel,
            navigateToAddEditTask = onTaskClicked
        )
    }
}