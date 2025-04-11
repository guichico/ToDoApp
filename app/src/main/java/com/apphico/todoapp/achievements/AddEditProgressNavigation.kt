package com.apphico.todoapp.achievements

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Progress
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.composable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val MEASUREMENT_TYPE_ARG = "measurement_type"
const val PROGRESS_ARG = "progress"

@Serializable
data class AddEditProgressRoute(val addEditProgressParameters: AddEditProgressParameters)

@Parcelize
@Serializable
data class AddEditProgressParameters(val measurementType: Int) : Parcelable

fun NavController.navigateToAddEditProgress(measurementType: Int) =
    navigate(AddEditProgressRoute(AddEditProgressParameters(measurementType)))

fun NavController.navigateBackToAddEditAchievement(savedStateHandle: SavedStateHandle, measurementType: Int, progress: Progress) {
    savedStateHandle.set<Int>(MEASUREMENT_TYPE_ARG, measurementType)
    savedStateHandle.set<Progress>(PROGRESS_ARG, progress)
    popBackStack<AddEditAchievementRoute>(inclusive = false)
}

fun NavGraphBuilder.addEditProgressScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    onProgressAdded: (SavedStateHandle, Int, Progress) -> Unit,
) {
    composable<AddEditProgressRoute, AddEditAchievementViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        typeMap = mapOf(
            typeOf<AddEditProgressParameters>() to CustomNavType(
                AddEditProgressParameters::class.java,
                AddEditProgressParameters.serializer()
            )
        )
    ) { previousSavedStateHandle ->
        AddEditProgressScreen(
            navigateBack = onBackClicked,
            onProgressAdded = { measurementType, progress -> onProgressAdded(previousSavedStateHandle, measurementType, progress) }
        )
    }
}
