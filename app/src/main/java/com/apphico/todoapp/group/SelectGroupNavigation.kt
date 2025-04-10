package com.apphico.todoapp.group

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Group
import com.apphico.todoapp.achievements.AddEditAchievementRoute
import com.apphico.todoapp.achievements.AddEditAchievementViewModel
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import com.apphico.todoapp.navigation.composable
import com.apphico.todoapp.navigation.navigateBack
import com.apphico.todoapp.task.AddEditTaskRoute
import com.apphico.todoapp.task.AddEditTaskViewModel
import kotlinx.serialization.Serializable

const val GROUP_ARG = "group"

@Serializable
object SelectGroupRoute

fun NavController.navigateToSelectGroup() = navigate(SelectGroupRoute)

fun NavController.navigateBackToAddEditTask(savedStateHandle: SavedStateHandle, group: Group) {
    savedStateHandle.set<Group>(GROUP_ARG, group)
    popBackStack<AddEditTaskRoute>(inclusive = false)
}

fun NavController.navigateBackToAddEditAchievement(savedStateHandle: SavedStateHandle, group: Group) {
    savedStateHandle.set<Group>(GROUP_ARG, group)
    popBackStack<AddEditAchievementRoute>(inclusive = false)
}

fun NavGraphBuilder.selectGroupScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    navController: NavController
) {
    selectGroupScreen<AddEditTaskViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        onGroupSelected = navController::navigateBackToAddEditTask,
        onEditClicked = navController::navigateToAddEditGroup,
    )
    selectGroupScreen<AddEditAchievementViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        onBackClicked = navController::navigateBack,
        onGroupSelected = navController::navigateBackToAddEditAchievement,
        onEditClicked = navController::navigateToAddEditGroup,
    )
}

private inline fun <reified VM : SavedStateHandleViewModel> NavGraphBuilder.selectGroupScreen(
    crossinline previousBackStackEntry: () -> NavBackStackEntry,
    noinline onBackClicked: () -> Unit,
    crossinline onGroupSelected: (SavedStateHandle, Group) -> Unit,
    noinline onEditClicked: (Group?) -> Unit
) {
    composable<SelectGroupRoute, VM>(
        previousBackStackEntry = previousBackStackEntry
    ) { previousSavedStateHandle ->
        SelectGroupScreen(
            navigateBack = onBackClicked,
            onEditGroupClicked = onEditClicked,
            onGroupSelected = { group -> onGroupSelected(previousSavedStateHandle, group) }
        )
    }
}