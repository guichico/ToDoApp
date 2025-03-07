package com.apphico.todoapp.navigation

import android.os.Bundle
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination.Companion.createRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.apphico.todoapp.achievements.AchievementsScreen
import com.apphico.todoapp.achievements.AddEditAchievementScreen
import com.apphico.todoapp.achievements.AddEditProgressScreen
import com.apphico.todoapp.achievements.enterAchievements
import com.apphico.todoapp.achievements.enterAddEditAchievement
import com.apphico.todoapp.achievements.enterAddEditProgress
import com.apphico.todoapp.achievements.exitAchievements
import com.apphico.todoapp.achievements.exitAddEditAchievement
import com.apphico.todoapp.achievements.exitAddEditProgress
import com.apphico.todoapp.achievements.navigateToAddEditAchievement
import com.apphico.todoapp.achievements.navigateToAddEditProgress
import com.apphico.todoapp.calendar.CalendarScreen
import com.apphico.todoapp.calendar.enterCalendar
import com.apphico.todoapp.calendar.exitCalendar
import com.apphico.todoapp.focus.AddEditFocusScreen
import com.apphico.todoapp.focus.FocusScreen
import com.apphico.todoapp.group.AddEditGroupScreen
import com.apphico.todoapp.group.SelectGroupScreen
import com.apphico.todoapp.group.enterAddEditGroup
import com.apphico.todoapp.group.enterSelectGroup
import com.apphico.todoapp.group.exitAddEditGroup
import com.apphico.todoapp.group.exitSelectGroup
import com.apphico.todoapp.group.navigateToAddEditGroup
import com.apphico.todoapp.group.navigateToSelectGroup
import com.apphico.todoapp.task.AddEditLocationScreen
import com.apphico.todoapp.task.AddEditTaskScreen
import com.apphico.todoapp.task.SelectLocationOnMapScreen
import com.apphico.todoapp.task.enterAddEditLocation
import com.apphico.todoapp.task.enterAddEditTask
import com.apphico.todoapp.task.enterSelectLocationOnMap
import com.apphico.todoapp.task.exitAddEditLocation
import com.apphico.todoapp.task.exitAddEditTask
import com.apphico.todoapp.task.exitSelectLocationOnMap
import com.apphico.todoapp.task.navigateBackToAddEditLocation
import com.apphico.todoapp.task.navigateBackToAddEditTask
import com.apphico.todoapp.task.navigateToAddEditLocation
import com.apphico.todoapp.task.navigateToAddEditTask
import com.apphico.todoapp.task.navigateToSelectLocationOnMap

const val DEFAULT_TWEEN_ANIMATION = 500

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    snackBar: (String) -> Unit
) {
    navigation(route = Flow.Main.route, startDestination = BottomBarNavigationItem.CALENDAR.route) {
        composable(
            route = BottomBarNavigationItem.FOCUS.route,
            enterTransition = { enterToEnd() },
            popEnterTransition = { enterToEnd() },
            exitTransition = { exitToStart() },
            popExitTransition = { exitToStart() },
        ) {
            FocusScreen()
        }
        composable(
            route = BottomBarNavigationItem.CALENDAR.route,
            enterTransition = { enterCalendar() },
            popEnterTransition = { enterCalendar() },
            exitTransition = { exitCalendar() },
            popExitTransition = { exitCalendar() },
        ) {
            CalendarScreen(
                navigateToAddEditTask = {
                    navController.navigateToAddEditTask(it)
                }
            )
        }
        composable(
            route = BottomBarNavigationItem.ACHIEVEMENT.route,
            enterTransition = { enterAchievements() },
            popEnterTransition = { enterAchievements() },
            exitTransition = { exitAchievements() },
            popExitTransition = { exitAchievements() },
        ) {
            AchievementsScreen(
                navigateToAddEditAchievement = { navController.navigateToAddEditAchievement(it) }
            )
        }
        composable(
            route = Screen.AddEditFocus.route,
            enterTransition = { enterToEnd() },
            popEnterTransition = { enterToEnd() },
            exitTransition = { exitToStart() },
            popExitTransition = { exitToStart() },
        ) {
            AddEditFocusScreen(
                navigateBack = { navController.navigateBack() }
            )
        }
        composable(
            route = Screen.AddEditTask.route,
            enterTransition = { enterAddEditTask() },
            popEnterTransition = { enterAddEditTask() },
            exitTransition = { exitAddEditTask() },
            popExitTransition = { exitAddEditTask() },
        ) {
            AddEditTaskScreen(
                navigateToSelectGroup = { navController.navigateToSelectGroup() },
                navigateToSelectLocation = { task, location -> navController.navigateToAddEditLocation(task, location) },
                navigateBack = { navController.navigateBack() },
                snackBar = snackBar
            )
        }
        composable(
            route = Screen.AddEditAchievement.route,
            enterTransition = { enterAddEditAchievement() },
            popEnterTransition = { enterAddEditAchievement() },
            exitTransition = { exitAddEditAchievement() },
            popExitTransition = { exitAddEditAchievement() },
        ) {
            AddEditAchievementScreen(
                navigateToSelectGroup = { navController.navigateToSelectGroup() },
                navigateToAddEditProgress = { navController.navigateToAddEditProgress() },
                navigateBack = { navController.navigateBack() }
            )
        }
        composable(
            route = Screen.SelectGroup.route,
            enterTransition = { enterSelectGroup() },
            popEnterTransition = { enterSelectGroup() },
            exitTransition = { exitSelectGroup() },
            popExitTransition = { exitSelectGroup() },
        ) {
            SelectGroupScreen(
                navigateToAddEditGroup = { navController.navigateToAddEditGroup() },
                navigateBack = { navController.navigateBack() }
            )
        }
        composable(
            route = Screen.AddEditGroup.route,
            enterTransition = { enterAddEditGroup() },
            popEnterTransition = { enterAddEditGroup() },
            exitTransition = { exitAddEditGroup() },
            popExitTransition = { exitAddEditGroup() },
        ) {
            AddEditGroupScreen(
                navigateBack = { navController.navigateBack() }
            )
        }
        composable(
            route = Screen.AddEditLocation.route,
            enterTransition = { enterAddEditLocation() },
            popEnterTransition = { enterAddEditLocation() },
            exitTransition = { exitAddEditLocation() },
            popExitTransition = { exitAddEditLocation() },
        ) {
            AddEditLocationScreen(
                navigateToSelectLocationOnMap = { task, location -> navController.navigateToSelectLocationOnMap(task, location) },
                onConfirmClicked = { task, location -> navController.navigateBackToAddEditTask(task, location) },
                navigateBack = { navController.navigateBack() }
            )
        }
        composable(
            route = Screen.SelectLocationOnMap.route,
            enterTransition = { enterSelectLocationOnMap() },
            popEnterTransition = { enterSelectLocationOnMap() },
            exitTransition = { exitSelectLocationOnMap() },
            popExitTransition = { exitSelectLocationOnMap() },
        ) {
            SelectLocationOnMapScreen(
                navigateBackToAddEditLocation = { task, location -> navController.navigateBackToAddEditLocation(task, location) },
                navigateBack = { navController.navigateBack() }
            )
        }
        composable(
            route = Screen.AddEditProgress.route,
            enterTransition = { enterAddEditProgress() },
            popEnterTransition = { enterAddEditProgress() },
            exitTransition = { exitAddEditProgress() },
            popExitTransition = { exitAddEditProgress() },
        ) {
            AddEditProgressScreen(
                navigateBack = { navController.navigateBack() }
            )
        }
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterToStart(): EnterTransition =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Start, tween(DEFAULT_TWEEN_ANIMATION)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterToEnd(): EnterTransition =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End, tween(DEFAULT_TWEEN_ANIMATION)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitToStart(): ExitTransition =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Start, tween(DEFAULT_TWEEN_ANIMATION)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitToEnd(): ExitTransition =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.End, tween(DEFAULT_TWEEN_ANIMATION)
    )

fun NavController.navigateBack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}

fun NavController.navigateWithArgs(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val routeLink = NavDeepLinkRequest.Builder.fromUri(createRoute(route).toUri()).build()

    val deeplinkMatch = graph.matchDeepLink(routeLink)
    deeplinkMatch?.let {
        navigate(it.destination.id, args, navOptions, navigatorExtras)
    } ?: navigate(route, navOptions, navigatorExtras)
}
