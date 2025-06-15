package com.apphico.todoapp.achievements

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.fakeData.mockedAchievements
import com.apphico.designsystem.achievements.AchievementCard
import com.apphico.designsystem.components.list.MainLazyList
import java.time.LocalDate

@Composable
fun AchievementsScreen(
    achievementsViewModel: AchievementsViewModel,
    navigateToAddEditAchievement: (Achievement?) -> Unit,


    isCalendarExpanded: State<Boolean>,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit,
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



        isCalendarExpanded = isCalendarExpanded,
        selectedDate = selectedDate,
        onSelectedDateChanged = onSelectedDateChanged,
        isFilterExpanded = isFilterExpanded,
        selectedStatus = selectedStatus,
        onStatusChanged = onStatusChanged,
        groups = groups,
        selectedGroups = selectedGroups,
        onGroupSelected = onGroupSelected,
        onSearchClicked = onSearchClicked
    )
}

@Composable
private fun AchievementsScreenContent(
    achievements: State<List<Achievement>>,
    navigateToAddEditAchievement: (Achievement?) -> Unit,
    onCheckListItemDoneChanged: (CheckListItem, LocalDate?, Boolean) -> Unit,


    isCalendarExpanded: State<Boolean>,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit,
) {
    val list = achievements.value

    MainLazyList(
        listState = rememberLazyListState(),
        onAddClicked = { navigateToAddEditAchievement(null) },


        isCalendarExpanded = isCalendarExpanded,
        selectedDate = selectedDate,
        onSelectedDateChanged = onSelectedDateChanged,
        isFilterExpanded = isFilterExpanded,
        selectedStatus = selectedStatus,
        onStatusChanged = onStatusChanged,
        groups = groups,
        selectedGroups = selectedGroups,
        onGroupSelected = onGroupSelected,
        onSearchClicked = onSearchClicked
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
    /*
    ToDoAppTheme {
        AchievementsScreenContent(
            achievements = remember { mutableStateOf(achievements) },
            navigateToAddEditAchievement = {},
            onCheckListItemDoneChanged = { _, _, _ -> }
        )
    }
     */
}
