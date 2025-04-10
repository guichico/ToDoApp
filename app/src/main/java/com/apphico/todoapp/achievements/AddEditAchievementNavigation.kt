package com.apphico.todoapp.achievements

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Achievement
import com.apphico.todoapp.navigation.CustomNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class AddEditAchievementRoute(val addEditAchievementParameters: AddEditAchievementParameters)

@Parcelize
@Serializable
data class AddEditAchievementParameters(val achievement: Achievement?) : Parcelable

fun NavController.navigateToAddEditAchievement(achievement: Achievement?) =
    navigate(AddEditAchievementRoute(AddEditAchievementParameters(achievement)))

fun NavGraphBuilder.addEditAchievementScreen(
    snackBar: (String) -> Unit,
    onBackClicked: () -> Unit,
    onSelectGroupClicked: () -> Unit,
    onAddEditProgressClicked: () -> Unit,
) {
    composable<AddEditAchievementRoute>(
        typeMap = mapOf(
            typeOf<AddEditAchievementParameters>() to CustomNavType(
                AddEditAchievementParameters::class.java,
                AddEditAchievementParameters.serializer()
            )
        )
    ) {
        AddEditAchievementScreen(
            snackBar = snackBar,
            navigateToSelectGroup = onSelectGroupClicked,
            navigateToAddEditProgress = onAddEditProgressClicked,
            navigateBack = onBackClicked
        )
    }
}
