package com.apphico.todoapp.group

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Group
import com.apphico.todoapp.navigation.composable
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

fun NavGraphBuilder.selectGroupScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    onGroupSelected: (SavedStateHandle, Group) -> Unit,
    onEditClicked: (Group?) -> Unit
) {
    composable<SelectGroupRoute, AddEditTaskViewModel>(
        previousBackStackEntry = previousBackStackEntry
    ) { previousSavedStateHandle ->
        SelectGroupScreen(
            navigateBack = onBackClicked,
            onEditGroupClicked = onEditClicked,
            onGroupSelected = { group -> onGroupSelected(previousSavedStateHandle, group) }
        )
    }
}