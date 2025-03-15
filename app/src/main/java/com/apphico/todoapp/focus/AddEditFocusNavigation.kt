package com.apphico.todoapp.focus

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.FocusMode
import com.apphico.todoapp.navigation.CustomNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class AddEditFocusRoute(val addEditFocusParameters: AddEditFocusParameters)

@Parcelize
@Serializable
data class AddEditFocusParameters(val focusMode: FocusMode?) : Parcelable

fun NavController.navigateToAddEditFocus(focusMode: FocusMode?) = navigate(AddEditFocusRoute(AddEditFocusParameters(focusMode)))

fun NavGraphBuilder.addEditFocusScreen(
    onBackClicked: () -> Unit
) {
    composable<AddEditFocusRoute>(
        typeMap = mapOf(
            typeOf<AddEditFocusParameters>() to CustomNavType(
                AddEditFocusParameters::class.java,
                AddEditFocusParameters.serializer()
            )
        )
    ) {
        AddEditFocusScreen(
            navigateBack = onBackClicked
        )
    }
}
