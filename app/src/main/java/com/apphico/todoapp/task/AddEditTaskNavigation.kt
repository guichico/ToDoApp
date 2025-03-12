package com.apphico.todoapp.task

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.todoapp.calendar.CalendarRoute
import com.apphico.todoapp.calendar.CalendarViewModel
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.composable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val SHOULD_REFRESH_ARG = "should_refresh"

@Serializable
data class AddEditTaskRoute(val addEditTaskParameters: AddEditTaskParameters)

@Parcelize
@Serializable
data class AddEditTaskParameters(val task: Task?) : Parcelable

fun NavController.navigateToAddEditTask(task: Task?) = navigate(AddEditTaskRoute(AddEditTaskParameters(task)))

fun NavController.navigateBackToCalendar(savedStateHandle: SavedStateHandle, shouldRefresh: Boolean) {
    savedStateHandle.set<Boolean>(SHOULD_REFRESH_ARG, shouldRefresh)
    popBackStack<CalendarRoute>(inclusive = false)
}

fun NavGraphBuilder.addEditTaskScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    snackBar: (String) -> Unit,
    onSelectGroupClicked: () -> Unit,
    onSelectLocationClicked: (Location?) -> Unit,
    onTaskSaved: (SavedStateHandle, Boolean) -> Unit,
) {
    composable<AddEditTaskRoute, CalendarViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        typeMap = mapOf(
            typeOf<AddEditTaskParameters>() to CustomNavType(
                AddEditTaskParameters::class.java,
                AddEditTaskParameters.serializer()
            )
        )
    ) { previousSavedStateHandle ->
        AddEditTaskScreen(
            snackBar = snackBar,
            navigateToSelectGroup = onSelectGroupClicked,
            navigateToSelectLocation = onSelectLocationClicked,
            onTaskSaved = { shouldRefresh -> onTaskSaved(previousSavedStateHandle, shouldRefresh) }
        )
    }
}