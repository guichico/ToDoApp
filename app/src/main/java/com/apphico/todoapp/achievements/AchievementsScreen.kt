package com.apphico.todoapp.achievements

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.Achievement
import com.apphico.core_model.fakeData.mockedAchievements
import com.apphico.designsystem.achievements.AchievementCard
import com.apphico.designsystem.components.list.MainLazyList
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun AchievementsScreen(
    achievementsViewModel: AchievementsViewModel = hiltViewModel(),
    navigateToAddEditAchievement: (Achievement?) -> Unit
) {
    val achievements = achievementsViewModel.achievements.collectAsState()

    AchievementsScreenContent(
        achievements = achievements,
        navigateToAddEditAchievement = navigateToAddEditAchievement
    )
}

@Composable
private fun AchievementsScreenContent(
    achievements: State<List<Achievement>>,
    navigateToAddEditAchievement: (Achievement?) -> Unit
) {
    MainLazyList(
        listState = rememberLazyListState(),
        onAddClicked = { navigateToAddEditAchievement(null) }
    ) {
        items(achievements.value) {
            AchievementCard(
                achievement = it,
                onClick = { navigateToAddEditAchievement(it) }
            )
        }
    }
}

class AchievementsScreenPreviewProvider : PreviewParameterProvider<List<Achievement>> {
    override val values = sequenceOf(mockedAchievements)
}

@PreviewLightDark
@Composable
private fun AchievementsScreenPreview(
    @PreviewParameter(AchievementsScreenPreviewProvider::class) achievements: List<Achievement>
) {
    ToDoAppTheme {
        AchievementsScreenContent(
            achievements = remember { mutableStateOf(achievements) },
            navigateToAddEditAchievement = {}
        )
    }
}
