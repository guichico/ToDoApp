package com.apphico.todoapp.navigation

import androidx.annotation.StringRes
import com.apphico.designsystem.R
import com.apphico.designsystem.theme.BaseToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.todoapp.achievements.AchievementsRoute
import com.apphico.todoapp.calendar.CalendarRoute
import com.apphico.todoapp.focus.FocusRoute

data class TopLevelRoute<T : Any>(val route: T, @StringRes val name: Int, val selectedIcon: BaseToDoAppIcon, val unselectedIcon: BaseToDoAppIcon)

val topLevelRoutes = listOf(
    TopLevelRoute(FocusRoute, R.string.menu_focus, ToDoAppIcons.icMenuFocusSelected, ToDoAppIcons.icMenuFocusUnselected),
    TopLevelRoute(CalendarRoute, R.string.menu_calendar, ToDoAppIcons.icMenuCalendarSelected, ToDoAppIcons.icMenuCalendarUnselected),
    TopLevelRoute(AchievementsRoute, R.string.menu_achievements, ToDoAppIcons.icMenuAchievementSelected, ToDoAppIcons.icMenuAchievementUnselected)
)