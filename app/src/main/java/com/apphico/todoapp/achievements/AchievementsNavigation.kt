package com.apphico.todoapp.achievements

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apphico.core_model.Achievement
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
object AchievementsRoute

fun NavGraphBuilder.achievementScreen(
    achievementsViewModel: AchievementsViewModel,
    onAchievementClicked: (Achievement?) -> Unit,

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
    composable<AchievementsRoute> {
        AchievementsScreen(
            achievementsViewModel = achievementsViewModel,
            navigateToAddEditAchievement = onAchievementClicked,


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
}