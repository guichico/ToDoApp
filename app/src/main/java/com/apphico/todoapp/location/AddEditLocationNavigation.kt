package com.apphico.todoapp.location

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Location
import com.apphico.todoapp.navigation.CustomNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class AddEditLocationRoute(val addEditLocationParameters: AddEditLocationParameters)

@Parcelize
@Serializable
data class AddEditLocationParameters(val location: Location) : Parcelable

fun NavController.navigateToAddEditLocation(location: Location) = navigate(AddEditLocationRoute(AddEditLocationParameters(location)))

fun NavGraphBuilder.addEditLocationScreen(
    onBackClick: () -> Unit,
    onConfirmClicked: (Location?) -> Unit,
    onSelectLocationOnMapClicked: (Location?) -> Unit
) {
    composable<AddEditLocationRoute>(
        typeMap = mapOf(
            typeOf<AddEditLocationParameters>() to CustomNavType(
                AddEditLocationParameters::class.java,
                AddEditLocationParameters.serializer()
            )
        )
    ) {
        AddEditLocationScreen(
            onConfirmClicked = onConfirmClicked,
            navigateToSelectLocationOnMap = onSelectLocationOnMapClicked,
            navigateBack = onBackClick
        )
    }
}