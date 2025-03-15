package com.apphico.todoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroups
import com.apphico.designsystem.components.date.CalendarView
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.snackbar.SnackBar
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.views.FilterView
import com.apphico.designsystem.views.Status
import com.apphico.extensions.formatDayAndMonth
import com.apphico.extensions.formatMediumDate
import com.apphico.extensions.getNowDate
import com.apphico.extensions.isCurrentYear
import com.apphico.todoapp.calendar.CalendarRoute
import com.apphico.todoapp.calendar.CalendarViewMode
import com.apphico.todoapp.focus.FocusRoute
import com.apphico.todoapp.navigation.ToDoAppBottomBar
import com.apphico.todoapp.navigation.TopLevelRoute
import com.apphico.todoapp.navigation.mainGraph
import com.apphico.todoapp.navigation.topLevelRoutes
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun AppScaffold(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {
    val coroutine = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val bottomBarSelectedItem = topLevelRoutes.firstOrNull { navBackStackEntry?.destination?.hasRoute(it.route::class) == true }

    var isBottomBarVisible by remember { mutableStateOf(true) }
    LaunchedEffect(bottomBarSelectedItem) {
        isBottomBarVisible = bottomBarSelectedItem != null || navBackStackEntry?.destination?.route == null
    }

    val calendarViewMode = remember { mutableStateOf(CalendarViewMode.DAY) }
    val selectedDate = remember { mutableStateOf(getNowDate()) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                SnackBar(
                    text = data.visuals.message
                )
            }
        },
        topBar = {
            if (isBottomBarVisible) {
                TopBar(
                    navBackStackEntry = navBackStackEntry,
                    bottomBarSelectedItem = bottomBarSelectedItem,
                    calendarViewMode = calendarViewMode,
                    onViewModeChanged = {
                        calendarViewMode.value = when (calendarViewMode.value) {
                            CalendarViewMode.DAY -> CalendarViewMode.AGENDA
                            CalendarViewMode.AGENDA -> CalendarViewMode.DAY
                        }
                    },
                    selectedDate = selectedDate,
                    onSelectedDateChanged = { selectedDate.value = it }
                )
            }
        },
        bottomBar = {
            if (isBottomBarVisible) {
                ToDoAppBottomBar(navController = navController)
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .padding(padding),
            navController = navController,
            startDestination = CalendarRoute
        ) {
            mainGraph(
                navController = navController,
                snackBar = {
                    coroutine.launch {
                        snackBarHostState.showSnackbar(it)
                    }
                },
                calendarViewMode = calendarViewMode,
                selectedDate = selectedDate
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navBackStackEntry: NavBackStackEntry?,
    bottomBarSelectedItem: TopLevelRoute<*>?,
    calendarViewMode: State<CalendarViewMode>,
    onViewModeChanged: () -> Unit,
    selectedDate: State<LocalDate>,
    onSelectedDateChanged: (LocalDate) -> Unit
) {
    val isCalendarSelected = bottomBarSelectedItem?.route is CalendarRoute
    val isFocusSelected = bottomBarSelectedItem?.route is FocusRoute

    val topBarTitle = bottomBarSelectedItem?.name?.let { stringResource(id = it) } ?: ""
    val topBarSubTitle = when {
        isCalendarSelected -> {
            val date = selectedDate.value
            if (date.isCurrentYear()) selectedDate.value.formatDayAndMonth() else selectedDate.value.formatMediumDate()
        }

        else -> null
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val isCalendarExpanded = remember { mutableStateOf(false) }
    val isFilterExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(navBackStackEntry) {
        isCalendarExpanded.value = false
        isFilterExpanded.value = false

        scrollBehavior.state.heightOffset = 0f
    }

    val onTitleClicked = {
        isFilterExpanded.value = false
        isCalendarExpanded.value = !isCalendarExpanded.value
    }

    val selectedStatus = remember { mutableStateOf(Status.ALL) }
    val selectedGroups = remember { mutableStateOf(mockedGroups.dropLast(2)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, spotColor = Color.Transparent)
    ) {
        ToDoAppTopBar(
            title = topBarTitle,
            onTitleClicked = if (isCalendarSelected) onTitleClicked else emptyLambda,
            subTitle = topBarSubTitle,
            scrollBehavior = scrollBehavior,
            actions = {
                FiltersButtonsRow(
                    isCalendarSelected = isCalendarSelected,
                    onSelectedDateChanged = onSelectedDateChanged,
                    calendarViewMode = calendarViewMode,
                    onViewModeChanged = onViewModeChanged,
                    onOpenFiltersClicked = {
                        isCalendarExpanded.value = false
                        isFilterExpanded.value = !isFilterExpanded.value
                    }
                )
            }
        )
        CalendarView(
            isCalendarExpanded = isCalendarExpanded,
            selectedDate = selectedDate,
            onSelectedDateChanged = onSelectedDateChanged
        )
        FilterView(
            isFilterExpanded = isFilterExpanded,
            showStatusFilter = !isFocusSelected,
            selectedStatus = selectedStatus,
            onStatusChanged = { selectedStatus.value = it },
            groups = remember { mutableStateOf(mockedGroups) },
            selectedGroups = selectedGroups,
            onGroupSelected = { selectedGroups.value = selectedGroups.value.addOrRemoveGroup(it) }
        )
    }
}

@Composable
private fun FiltersButtonsRow(
    isCalendarSelected: Boolean,
    onSelectedDateChanged: (LocalDate) -> Unit,
    calendarViewMode: State<CalendarViewMode>,
    onViewModeChanged: () -> Unit,
    onOpenFiltersClicked: () -> Unit
) {
    Row {
        if (isCalendarSelected) {
            ToDoAppIconButton(
                icon = ToDoAppIcons.icToday,
                onClick = { onSelectedDateChanged(getNowDate()) }
            )
            ToDoAppIconButton(
                icon = if (calendarViewMode.value == CalendarViewMode.DAY) ToDoAppIcons.icCalendarViewDay else ToDoAppIcons.icCalendarViewAgenda,
                onClick = onViewModeChanged
            )
        }
        ToDoAppIconButton(
            icon = ToDoAppIcons.icFilter,
            onClick = onOpenFiltersClicked
        )
    }
}

private fun List<Group>.addOrRemoveGroup(group: Group): List<Group> {
    return this.toMutableList().apply {
        if (this.contains(group)) {
            remove(group)
        } else {
            add(group)
        }
    }
}