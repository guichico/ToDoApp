package com.apphico.todoapp.calendar

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Task
import com.apphico.designsystem.components.list.ToDoAppNestedScroll
import kotlinx.serialization.Serializable

@Serializable
object CalendarRoute

fun NavGraphBuilder.calendarScreen(
    calendarViewModel: CalendarViewModel,
    tasksNestedScroll: ToDoAppNestedScroll,
    onTaskClicked: (Task?) -> Unit
) {
    composable<CalendarRoute> {
        CalendarScreen(
            calendarViewModel = calendarViewModel,
            tasksNestedScroll = tasksNestedScroll,
            navigateToAddEditTask = onTaskClicked
        )
    }
}