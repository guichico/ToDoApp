package com.apphico.todoapp.focus

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.FocusMode
import kotlinx.serialization.Serializable

@Serializable
object FocusRoute

fun NavGraphBuilder.focusScreen(
    onFocusClicked: (FocusMode?) -> Unit
) {
    composable<FocusRoute> {
        FocusScreen(
            navigateToAddEditFocus = onFocusClicked
        )
    }
}