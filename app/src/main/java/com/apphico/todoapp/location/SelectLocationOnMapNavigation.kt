package com.apphico.todoapp.location

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.apphico.core_model.Coordinates
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.composable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val COORDINATES_ARG = "coordinates"

@Serializable
data class SelectLocationOnMapRoute(val selectLocationOnMapParameters: SelectLocationOnMapParameters)

@Parcelize
@Serializable
data class SelectLocationOnMapParameters(val coordinates: Coordinates?) : Parcelable

fun NavController.navigateToSelectLocationOnMap(coordinates: Coordinates?) =
    navigate(SelectLocationOnMapRoute(SelectLocationOnMapParameters(coordinates)))

fun NavController.navigateBackToAddEditLocation(savedStateHandle: SavedStateHandle, coordinates: Coordinates?) {
    savedStateHandle.set<Coordinates>(COORDINATES_ARG, coordinates)
    popBackStack<AddEditLocationRoute>(inclusive = false)
}

fun NavGraphBuilder.selectLocationOnMapScreen(
    previousBackStackEntry: () -> NavBackStackEntry,
    onBackClicked: () -> Unit,
    navigateBackToAddEditLocation: (SavedStateHandle, Coordinates?) -> Unit
) {
    composable<SelectLocationOnMapRoute, AddEditLocationViewModel>(
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
            navigateBackToAddEditLocation = { coordinates ->
                navigateBackToAddEditLocation(previousSavedStateHandle, coordinates)
            }
        )
    }
}
