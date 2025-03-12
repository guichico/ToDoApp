package com.apphico.todoapp.group

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Group
import com.apphico.todoapp.navigation.CustomNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class AddEditGroupRoute(val addEditGroupParameters: AddEditGroupParameters)

@Parcelize
@Serializable
data class AddEditGroupParameters(val group: Group?) : Parcelable

fun NavController.navigateToAddEditGroup(group: Group?) = navigate(AddEditGroupRoute(AddEditGroupParameters(group)))

fun NavGraphBuilder.addEditGroupScreen(
    onBackClick: () -> Unit
) {
    composable<AddEditGroupRoute>(
        typeMap = mapOf(
            typeOf<AddEditGroupParameters>() to CustomNavType(
                AddEditGroupParameters::class.java,
                AddEditGroupParameters.serializer()
            )
        )
    ) {
        AddEditGroupScreen(
            navigateBack = onBackClick
        )
    }
}