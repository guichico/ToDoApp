package com.apphico.todoapp.location

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Location
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.composable
import com.apphico.todoapp.task.AddEditTaskRoute
import com.apphico.todoapp.task.AddEditTaskViewModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val REMOVE_LOCATION_ARG = "remove_location"
const val LOCATION_ARG = "location"

@Serializable
data class AddEditLocationRoute(val addEditLocationParameters: AddEditLocationParameters)

@Parcelize
@Serializable
data class AddEditLocationParameters(val location: Location?) : Parcelable

fun NavController.navigateToAddEditLocation(location: Location?) = navigate(AddEditLocationRoute(AddEditLocationParameters(location)))

fun NavController.navigateBackToAddEditTaskWithAddLocation(savedStateHandle: SavedStateHandle, location: Location?) {
    savedStateHandle[LOCATION_ARG] = location
    popBackStack<AddEditTaskRoute>(inclusive = false)
}

fun NavController.navigateBackToAddEditTaskWithRemoveLocation(savedStateHandle: SavedStateHandle) {
    savedStateHandle[REMOVE_LOCATION_ARG] = true
    popBackStack<AddEditTaskRoute>(inclusive = false)
}

fun NavGraphBuilder.addEditLocationScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    onSelectLocationOnMapClicked: (Coordinates?) -> Unit,
    onRemoveClicked: (SavedStateHandle) -> Unit,
    onConfirmClicked: (SavedStateHandle, Location?) -> Unit
) {
    composable<AddEditLocationRoute, AddEditTaskViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        typeMap = mapOf(
            typeOf<AddEditLocationParameters>() to CustomNavType(
                AddEditLocationParameters::class.java,
                AddEditLocationParameters.serializer()
            )
        )
    ) { previousSavedStateHandle ->
        AddEditLocationScreen(
            navigateBack = onBackClicked,
            navigateToSelectLocationOnMap = onSelectLocationOnMapClicked,
            onRemoveClicked = { onRemoveClicked(previousSavedStateHandle) },
            onConfirmClicked = { location -> onConfirmClicked(previousSavedStateHandle, location) }
        )
    }
}
