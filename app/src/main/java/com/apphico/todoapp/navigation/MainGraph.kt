package com.apphico.todoapp.navigation

import androidx.compose.runtime.Composable
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
import com.apphico.todoapp.achievements.navigateBackToAddEditAchievement
import com.apphico.todoapp.achievements.navigateToAddEditAchievement
import com.apphico.todoapp.achievements.navigateToAddEditProgress
import com.apphico.todoapp.calendar.CalendarViewModel
import com.apphico.todoapp.calendar.calendarScreen
import com.apphico.todoapp.focus.addEditFocusScreen
import com.apphico.todoapp.focus.focusScreen
import com.apphico.todoapp.focus.navigateToAddEditFocus
import com.apphico.todoapp.group.SelectGroupRouteFromAchievement
import com.apphico.todoapp.group.SelectGroupRouteFromTask
import com.apphico.todoapp.group.addEditGroupScreen
import com.apphico.todoapp.group.navigateToAddEditGroup
import com.apphico.todoapp.group.navigateToSelectGroup
import com.apphico.todoapp.group.selectGroupScreen
import com.apphico.todoapp.location.addEditLocationScreen
import com.apphico.todoapp.location.navigateBackToAddEditLocation
import com.apphico.todoapp.location.navigateBackToAddEditTaskWithAddLocation
import com.apphico.todoapp.location.navigateBackToAddEditTaskWithRemoveLocation
import com.apphico.todoapp.location.navigateToAddEditLocation
import com.apphico.todoapp.location.navigateToSelectLocationOnMap
import com.apphico.todoapp.location.selectLocationOnMapScreen
import com.apphico.todoapp.task.addEditTaskScreen
import com.apphico.todoapp.task.navigateToAddEditTask
import kotlin.reflect.KType

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    snackBar: (String) -> Unit,
    calendarViewModel: CalendarViewModel,
) {
    val previousBackStackEntry = { navController.previousBackStackEntry!! }

    focusScreen(
        onFocusClicked = navController::navigateToAddEditFocus
    )
    calendarScreen(
        calendarViewModel = calendarViewModel,
        onTaskClicked = navController::navigateToAddEditTask
    )
    achievementScreen(
        onAchievementClicked = navController::navigateToAddEditAchievement
    )
    addEditTaskScreen(
        snackBar = snackBar,
        onBackClicked = navController::navigateBack,
        onSelectGroupClicked = { navController.navigateToSelectGroup(SelectGroupRouteFromTask) },
        onSelectLocationClicked = navController::navigateToAddEditLocation
    )
    selectGroupScreen(
        previousBackStackEntry = previousBackStackEntry,
        navController = navController,
        onBackClicked = navController::navigateBack,
        onEditClicked = navController::navigateToAddEditGroup
    )
    addEditGroupScreen(
        snackBar = snackBar,
        onBackClicked = navController::navigateBack
    )
    addEditLocationScreen(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        onSelectLocationOnMapClicked = navController::navigateToSelectLocationOnMap,
        onRemoveClicked = navController::navigateBackToAddEditTaskWithRemoveLocation,
        onConfirmClicked = navController::navigateBackToAddEditTaskWithAddLocation
    )
    selectLocationOnMapScreen(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        navigateBackToAddEditLocation = navController::navigateBackToAddEditLocation
    )
    addEditFocusScreen(
        onBackClicked = navController::navigateBack
    )
    addEditAchievementScreen(
        snackBar = snackBar,
        onBackClicked = navController::navigateBack,
        onSelectGroupClicked = { navController.navigateToSelectGroup(SelectGroupRouteFromAchievement) },
        onAddEditProgressClicked = navController::navigateToAddEditProgress
    )
    addEditProgressScreen(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        onProgressChanged = navController::navigateBackToAddEditAchievement
    )
}

fun NavController.navigateBack() {
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