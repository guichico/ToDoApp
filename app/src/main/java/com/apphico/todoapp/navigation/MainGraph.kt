package com.apphico.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.apphico.todoapp.achievements.achievementScreen
import com.apphico.todoapp.achievements.addEditAchievementScreen
import com.apphico.todoapp.achievements.addEditProgressScreen
import com.apphico.todoapp.achievements.navigateToAddEditAchievement
import com.apphico.todoapp.calendar.CalendarViewMode
import com.apphico.todoapp.calendar.calendarScreen
import com.apphico.todoapp.focus.addEditFocusScreen
import com.apphico.todoapp.focus.focusScreen
import com.apphico.todoapp.focus.navigateToAddEditFocus
import com.apphico.todoapp.group.addEditGroupScreen
import com.apphico.todoapp.group.navigateBackToAddEditTask
import com.apphico.todoapp.group.navigateToAddEditGroup
import com.apphico.todoapp.group.navigateToSelectGroup
import com.apphico.todoapp.group.selectGroupScreen
import com.apphico.todoapp.location.addEditLocationScreen
import com.apphico.todoapp.location.navigateBackToAddEditTask
import com.apphico.todoapp.location.navigateToSelectLocationOnMap
import com.apphico.todoapp.location.selectLocationOnMapScreen
import com.apphico.todoapp.task.addEditTaskScreen
import com.apphico.todoapp.task.navigateToAddEditTask
import java.time.LocalDate
import kotlin.reflect.KType

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    snackBar: (String) -> Unit,
    calendarViewMode: State<CalendarViewMode>,
    selectedDate: State<LocalDate>
) {
    val previousBackStackEntry = { navController.previousBackStackEntry!! }

    focusScreen(
        onFocusClicked = navController::navigateToAddEditFocus
    )
    calendarScreen(
        calendarViewMode = calendarViewMode,
        selectedDate = selectedDate,
        onTaskClicked = navController::navigateToAddEditTask
    )
    achievementScreen(
        onAchievementClicked = navController::navigateToAddEditAchievement
    )
    addEditTaskScreen(
        snackBar = snackBar,
        onBackClicked = navController::navigateBack,
        onSelectGroupClicked = navController::navigateToSelectGroup,
        onSelectLocationClicked = navController::navigateToSelectLocationOnMap
    )
    selectGroupScreen(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        onGroupSelected = navController::navigateBackToAddEditTask,
        onEditClicked = navController::navigateToAddEditGroup,
    )
    addEditGroupScreen(
        snackBar = snackBar,
        onBackClicked = navController::navigateBack
    )
    selectLocationOnMapScreen(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        onSearchFinished = navController::navigateBackToAddEditTask
    )
    addEditLocationScreen(
        onBackClicked = navController::navigateBack,
        onSelectLocationOnMapClicked = { location ->
            // navController.navigateToSelectLocationOnMap(task, location)
        },
        onConfirmClicked = { location ->
            // navController.navigateBackToAddEditTask(task, location)
        }
    )
    addEditFocusScreen(
        onBackClicked = navController::navigateBack
    )
    addEditAchievementScreen(
        onBackClicked = navController::navigateBack,
        onSelectGroupClicked = {},
        onAddEditProgressClicked = {}
    )
    addEditProgressScreen(
        onBackClicked = navController::navigateBack
    )
}

private fun NavController.navigateBack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}

inline fun <reified T : Any, reified VM : SavedStateHandleViewModel> NavGraphBuilder.composable(
    crossinline previousBackStackEntry: () -> NavBackStackEntry,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    noinline content: @Composable (SavedStateHandle) -> Unit
) {
    composable<T>(
        typeMap = typeMap
    ) { backStackEntry ->
        val previousBackStackEntry = remember(backStackEntry) { previousBackStackEntry() }
        val previousViewModel = hiltViewModel<VM>(previousBackStackEntry)

        content(previousViewModel.savedStateHandle)
    }
}