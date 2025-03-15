package com.apphico.todoapp.location

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Location
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.composable
import com.apphico.todoapp.task.AddEditTaskRoute
import com.apphico.todoapp.task.AddEditTaskViewModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val LOCATION_ARG = "location"

@Serializable
data class SelectLocationOnMapRoute(val selectLocationOnMapParameters: SelectLocationOnMapParameters)

@Parcelize
@Serializable
data class SelectLocationOnMapParameters(val location: Location?) : Parcelable

fun NavController.navigateToSelectLocationOnMap(location: Location?) = navigate(SelectLocationOnMapRoute(SelectLocationOnMapParameters(location)))

fun NavController.navigateBackToAddEditTask(savedStateHandle: SavedStateHandle, location: Location?) {
    savedStateHandle.set<Location>(LOCATION_ARG, location)
    popBackStack<AddEditTaskRoute>(inclusive = false)
}

fun NavGraphBuilder.selectLocationOnMapScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    onSearchFinished: (SavedStateHandle, Location?) -> Unit
) {
    composable<SelectLocationOnMapRoute, AddEditTaskViewModel>(
        previousBackStackEntry = previousBackStackEntry,
        typeMap = mapOf(
            typeOf<SelectLocationOnMapParameters>() to CustomNavType(
                SelectLocationOnMapParameters::class.java,
                SelectLocationOnMapParameters.serializer()
            )
        )
    ) { previousSavedStateHandle ->
        SelectLocationOnMapScreen(
            navigateBack = onBackClicked,
            onSearchFinished = { location -> onSearchFinished(previousSavedStateHandle, location) }
        )
    }
}
