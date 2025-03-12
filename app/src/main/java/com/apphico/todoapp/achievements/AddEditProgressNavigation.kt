package com.apphico.todoapp.achievements

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import kotlinx.serialization.Serializable

@Serializable
object AddEditProgressRoute

fun NavGraphBuilder.addEditProgressScreen(
    onBackClick: () -> Unit,
) {
    composable<AddEditProgressRoute> {
        AddEditProgressScreen(
            navigateBack = onBackClick
        )
    }
}
