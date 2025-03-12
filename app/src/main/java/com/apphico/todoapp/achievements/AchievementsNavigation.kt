package com.apphico.todoapp.achievements

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Achievement
import kotlinx.serialization.Serializable

@Serializable
object AchievementsRoute

fun NavGraphBuilder.achievementScreen(
    onAchievementClicked: (Achievement?) -> Unit
) {
    composable<AchievementsRoute> {
        AchievementsScreen(
            navigateToAddEditAchievement = onAchievementClicked
        )
    }
}