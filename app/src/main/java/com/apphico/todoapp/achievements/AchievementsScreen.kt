package com.apphico.todoapp.achievements

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.fakeData.mockedAchievements
import com.apphico.designsystem.achievements.AchievementCard
import com.apphico.designsystem.components.list.MainLazyList
import com.apphico.designsystem.theme.ToDoAppTheme
import java.time.LocalDate

@Composable
fun AchievementsScreen(
    achievementsViewModel: AchievementsViewModel,
    navigateToAddEditAchievement: (Achievement?) -> Unit,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    val achievements = achievementsViewModel.achievements.collectAsState()

    AchievementsScreenContent(
        achievements = achievements,
        navigateToAddEditAchievement = navigateToAddEditAchievement,
        onCheckListItemDoneChanged = { checkListItem, parentDate, isDone ->
            achievementsViewModel.setCheckListItemDone(
                checkListItem,
                parentDate,
                isDone
            )
        },
        isNestedViewExpanded = isNestedViewExpanded,
        onNestedViewClosed = onNestedViewClosed,
        nestedContent = nestedContent
    )
}

@Composable
private fun AchievementsScreenContent(
    achievements: State<List<Achievement>>,
    navigateToAddEditAchievement: (Achievement?) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,
    isNestedViewExpanded: State<Boolean>,
    onNestedViewClosed: () -> Unit,
    nestedContent: @Composable BoxScope.(modifier: Modifier) -> Unit,
) {
    val list = achievements.value

    MainLazyList(
        listState = rememberLazyListState(),
        onAddClicked = { navigateToAddEditAchievement(null) },
        isNestedViewExpanded = isNestedViewExpanded,
        onNestedViewClosed = onNestedViewClosed,
        nestedContent = nestedContent
    ) {
        items(list) {
            AchievementCard(
                achievement = it,
                onClick = { navigateToAddEditAchievement(it) },
                onCheckListItemDoneChanged = onCheckListItemDoneChanged
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
            navigateToAddEditAchievement = {},
            onCheckListItemDoneChanged = { _, _, _ -> },
            isNestedViewExpanded = remember { mutableStateOf(false) },
            onNestedViewClosed = {},
            nestedContent = {}
        )
    }
}
