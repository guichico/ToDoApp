package com.apphico.todoapp.calendar

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
object CalendarRoute

fun NavGraphBuilder.calendarScreen(
    calendarViewModel: CalendarViewModel,
    onTaskClicked: (Task?) -> Unit,


    isCalendarExpanded: State<Boolean>,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit,
) {
    composable<CalendarRoute> {
        CalendarScreen(
            calendarViewModel = calendarViewModel,
            navigateToAddEditTask = onTaskClicked,


            isCalendarExpanded = isCalendarExpanded,
            selectedDate = selectedDate,
            onSelectedDateChanged = onSelectedDateChanged,
            isFilterExpanded = isFilterExpanded,
            selectedStatus = selectedStatus,
            onStatusChanged = onStatusChanged,
            groups = groups,
            selectedGroups = selectedGroups,
            onGroupSelected = onGroupSelected,
            onSearchClicked = onSearchClicked
        )
    }
}