package com.apphico.todoapp.group

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Group
import com.apphico.todoapp.achievements.AddEditAchievementRoute
import com.apphico.todoapp.achievements.AddEditAchievementViewModel
import com.apphico.todoapp.navigation.composable
import com.apphico.todoapp.task.AddEditTaskRoute
import com.apphico.todoapp.task.AddEditTaskViewModel
import kotlinx.serialization.Serializable

const val GROUP_ARG = "group"

@Serializable
object SelectGroupRouteFromTask

@Serializable
object SelectGroupRouteFromAchievement

fun <T : Any> NavController.navigateToSelectGroup(route: T) = navigate(route)

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
    navController: NavController,
    onBackClicked: () -> Unit,
    onEditClicked: (Group?) -> Unit
) {
    composable<SelectGroupRouteFromTask, AddEditTaskViewModel>(
        previousBackStackEntry = previousBackStackEntry
    ) { previousSavedStateHandle ->
        SelectGroupScreen(
            navigateBack = onBackClicked,
            onEditGroupClicked = onEditClicked,
            onGroupSelected = { group ->
                navController.navigateBackToAddEditTask(previousSavedStateHandle, group)
            }
        )
    }
    composable<SelectGroupRouteFromAchievement, AddEditAchievementViewModel>(
        previousBackStackEntry = previousBackStackEntry
    ) { previousSavedStateHandle ->
        SelectGroupScreen(
            navigateBack = onBackClicked,
            onEditGroupClicked = onEditClicked,
            onGroupSelected = { group ->
                navController.navigateBackToAddEditAchievement(previousSavedStateHandle, group)
            }
        )
    }
}
