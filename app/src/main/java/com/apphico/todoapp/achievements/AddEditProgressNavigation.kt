package com.apphico.todoapp.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.MeasurementType
import com.apphico.todoapp.navigation.composable
import kotlinx.serialization.Serializable

const val PROGRESS_ARG = "progress"

@Serializable
object AddEditProgressRoute

fun NavController.navigateToAddEditProgress() = navigate(AddEditProgressRoute)

fun NavController.navigateBackToAddEditAchievement(
    savedStateHandle: SavedStateHandle,
    percentageProgress: MeasurementType.Percentage.PercentageProgress?
) {
    savedStateHandle.set<MeasurementType.Percentage.PercentageProgress>(PROGRESS_ARG, percentageProgress)
    popBackStack<AddEditAchievementRoute>(inclusive = false)
}

fun NavGraphBuilder.addEditProgressScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    onProgressAdded: (SavedStateHandle, MeasurementType.Percentage.PercentageProgress) -> Unit,
) {
    composable<AddEditProgressRoute, AddEditAchievementViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        typeMap = mapOf()
    ) { previousSavedStateHandle ->
        AddEditProgressScreen(
            navigateBack = onBackClicked,
            onProgressAdded = { progress -> onProgressAdded(previousSavedStateHandle, progress) }
        )
    }
}
