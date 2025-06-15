package com.apphico.todoapp.calendar

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Task
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
object CalendarRoute

fun NavGraphBuilder.calendarScreen(
    calendarViewModel: CalendarViewModel,
    onTaskClicked: (Task?) -> Unit,
    selectedDate: State<LocalDate>,
    anchorViewHeight: State<Dp>,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    composable<CalendarRoute> {
        CalendarScreen(
            calendarViewModel = calendarViewModel,
            navigateToAddEditTask = onTaskClicked,
            selectedDate = selectedDate,
            anchorViewHeight = anchorViewHeight,
            isNestedViewExpanded = isNestedViewExpanded,
            onNestedViewClosed = onNestedViewClosed,
            nestedContent = nestedContent
        )
    }
}