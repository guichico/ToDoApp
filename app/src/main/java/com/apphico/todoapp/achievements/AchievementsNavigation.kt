package com.apphico.todoapp.achievements

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Achievement
import kotlinx.serialization.Serializable

@Serializable
object AchievementsRoute

fun NavGraphBuilder.achievementScreen(
    achievementsViewModel: AchievementsViewModel,
    onAchievementClicked: (Achievement?) -> Unit,
    anchorViewHeight: State<Dp>,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    composable<AchievementsRoute> {
        AchievementsScreen(
            achievementsViewModel = achievementsViewModel,
            navigateToAddEditAchievement = onAchievementClicked,
            anchorViewHeight = anchorViewHeight,
            isNestedViewExpanded = isNestedViewExpanded,
            onNestedViewClosed = onNestedViewClosed,
            nestedContent = nestedContent
        )
    }
}