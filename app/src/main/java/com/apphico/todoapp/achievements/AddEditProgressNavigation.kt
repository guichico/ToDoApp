package com.apphico.todoapp.achievements

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.core_model.Progress
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.composable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val MEASUREMENT_TYPE_ARG = "measurement_type"
const val OPERATION_ARG = "operation"

@Parcelize
@Serializable
sealed class Operation(open val progress: Progress) : Parcelable {

    @Parcelize
    data class Save(override val progress: Progress) : Operation(progress)

    @Parcelize
    data class Update(val oldProgress: Progress, override val progress: Progress) : Operation(progress)

    @Parcelize
    data class Delete(override val progress: Progress) : Operation(progress)
}

@Serializable
data class AddEditProgressRoute(val addEditProgressParameters: AddEditProgressParameters)

@Parcelize
@Serializable
data class AddEditProgressParameters(
    val measurementType: Int,
    val measurementUnit: MeasurementValueUnit?,
    val progress: Progress?
) : Parcelable

fun NavController.navigateToAddEditProgress(measurementType: Int, measurementUnit: MeasurementValueUnit?, progress: Progress?) =
    navigate(AddEditProgressRoute(AddEditProgressParameters(measurementType, measurementUnit, progress)))

fun NavController.navigateBackToAddEditAchievement(savedStateHandle: SavedStateHandle, measurementType: Int, operation: Operation) {
    savedStateHandle.set<Int>(MEASUREMENT_TYPE_ARG, measurementType)
    savedStateHandle.set<Operation>(OPERATION_ARG, operation)
    popBackStack<AddEditAchievementRoute>(inclusive = false)
}

fun NavGraphBuilder.addEditProgressScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    onProgressChanged: (SavedStateHandle, Int, operation: Operation) -> Unit,
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
            onProgressChanged = { measurementType, operation ->
                onProgressChanged(previousSavedStateHandle, measurementType, operation)
            }
        )
    }
}
