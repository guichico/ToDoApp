package com.apphico.todoapp.task

import android.os.Parcelable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.todoapp.navigation.CustomNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class AddEditTaskRoute(val addEditTaskParameters: AddEditTaskParameters)

@Parcelize
@Serializable
data class AddEditTaskParameters(val task: Task?) : Parcelable

fun NavController.navigateToAddEditTask(task: Task?) = navigate(AddEditTaskRoute(AddEditTaskParameters(task)))

fun NavGraphBuilder.addEditTaskScreen(
    snackBar: (String) -> Unit,
    onBackClicked: () -> Unit,
    onSelectGroupClicked: () -> Unit,
    onSelectLocationClicked: (Location?) -> Unit
) {
    composable<AddEditTaskRoute>(
        typeMap = mapOf(
            typeOf<AddEditTaskParameters>() to CustomNavType(
                AddEditTaskParameters::class.java,
                AddEditTaskParameters.serializer()
            )
        ),
        enterTransition = { slideInVertically(initialOffsetY = { it }) },
        popEnterTransition = { fadeIn() },
    ) { previousSavedStateHandle ->
        AddEditTaskScreen(
            snackBar = snackBar,
            navigateToSelectGroup = onSelectGroupClicked,
            navigateToSelectLocation = onSelectLocationClicked,
            navigateBack = onBackClicked
        )
    }
}