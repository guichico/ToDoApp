package com.apphico.todoapp.navigation

import androidx.annotation.StringRes
import com.apphico.designsystem.R
import com.apphico.designsystem.theme.BaseToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.todoapp.achievements.AchievementsRoute
import com.apphico.todoapp.calendar.CalendarRoute

data class TopLevelRoute<T : Any>(
    val route: T,
    @param:StringRes val name: Int,
    val selectedIcon: BaseToDoAppIcon,
    val unselectedIcon: BaseToDoAppIcon
)

val topLevelRoutes = listOf(
    // TODO Not yet implemented
    // TopLevelRoute(FocusRoute, R.string.menu_focus, ToDoAppIcons.icMenuFocusSelected, ToDoAppIcons.icMenuFocusUnselected),
    TopLevelRoute(
        route = CalendarRoute,
        name = R.string.menu_calendar,
        selectedIcon = ToDoAppIcons.icMenuCalendarSelected,
        unselectedIcon = ToDoAppIcons.icMenuCalendarUnselected
    ),
    TopLevelRoute(
        route = AchievementsRoute,
        name = R.string.menu_achievements,
        selectedIcon = ToDoAppIcons.icMenuAchievementSelected,
        unselectedIcon = ToDoAppIcons.icMenuAchievementUnselected
    )
)
