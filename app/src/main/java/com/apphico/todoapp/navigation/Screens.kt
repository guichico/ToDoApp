package com.apphico.todoapp.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavBackStackEntry
import com.apphico.designsystem.R
import com.apphico.designsystem.theme.BaseToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons

sealed class Flow(val route: String) {
    data object Main : Screen("main_flow")
}

interface BottomBarItem {
    val label: Int
    val selectedIcon: BaseToDoAppIcon
    val unselectedIcon: BaseToDoAppIcon
    val route: String
}

sealed class Screen(val route: String) {
    data object AddEditFocus : Screen("add_edit_focus_screen")
    data object AddEditTask : Screen("add_edit_task_screen")
    data object AddEditAchievement : Screen("add_edit_achievement_screen")
    data object SelectGroup : Screen("select_group_screen")
    data object AddEditGroup : Screen("add_edit_group_screen")
    data object AddEditLocation : Screen("add_edit_location_screen")
    data object SelectLocationOnMap : Screen("select_location_on_map_screen")
    data object AddEditProgress : Screen("add_edit_progress_screen")
}

enum class BottomBarNavigationItem(
    @StringRes override val label: Int,
    override val selectedIcon: BaseToDoAppIcon,
    override val unselectedIcon: BaseToDoAppIcon,
    override val route: String
) : BottomBarItem {
    FOCUS(R.string.menu_focus, ToDoAppIcons.icMenuFocusSelected, ToDoAppIcons.icMenuFocusUnselected, "focus_screen"),
    CALENDAR(R.string.menu_calendar, ToDoAppIcons.icMenuCalendarSelected, ToDoAppIcons.icMenuCalendarUnselected, "calendar_screen"),
    ACHIEVEMENT(R.string.menu_achievements, ToDoAppIcons.icMenuAchievementSelected, ToDoAppIcons.icMenuAchievementUnselected, "achievement_screen")
}
