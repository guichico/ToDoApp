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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.apphico.core_model.CalendarViewMode
import com.apphico.designsystem.components.date.CalendarView
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.snackbar.SnackBar
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.extensions.getNowDate
import com.apphico.todoapp.achievements.AchievementsViewModel
import com.apphico.todoapp.calendar.CalendarRoute
import com.apphico.todoapp.calendar.CalendarViewModel
import com.apphico.todoapp.navigation.ToDoAppBottomBar
import com.apphico.todoapp.navigation.TopLevelRoute
import com.apphico.todoapp.navigation.mainGraph
import com.apphico.todoapp.navigation.topLevelRoutes
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController
) {
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val achievementsViewModel: AchievementsViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarSelectedItem = topLevelRoutes.firstOrNull { currentDestination?.hasRoute(it.route::class) == true }
    var isCalendarSelected by remember { mutableStateOf(true) }

    var isBottomBarVisible by remember { mutableStateOf(true) }
    var filterViewModel: FilterViewModel by remember { mutableStateOf(calendarViewModel) }

    LaunchedEffect(bottomBarSelectedItem) {
        isBottomBarVisible = bottomBarSelectedItem != null && currentDestination?.route != null
        isCalendarSelected = bottomBarSelectedItem?.route is CalendarRoute

        filterViewModel = when {
            isCalendarSelected -> calendarViewModel
            else -> achievementsViewModel
        }
    }

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val coroutine = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val currentMonth = calendarViewModel.currentMonth.collectAsState()
    val selectedDate = calendarViewModel.selectedDate.collectAsState()
    val calendarViewMode = calendarViewModel.calendarViewMode.collectAsState()

    val isCalendarExpanded = remember { mutableStateOf(false) }
    val isFilterExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(navBackStackEntry) {
        isCalendarExpanded.value = false
        isFilterExpanded.value = false

        topBarScrollBehavior.state.heightOffset = 0f
    }

    val onTitleClicked = {
        isFilterExpanded.value = false
        isCalendarExpanded.value = !isCalendarExpanded.value
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                SnackBar(
                    text = data.visuals.message,
                    endPadding = if (isBottomBarVisible) 84.dp else ToDoAppTheme.spacing.large
                )
            }
        },
        topBar = {
            if (isBottomBarVisible) {
                TopBar(
                    isCalendarSelected = isCalendarSelected,
                    topBarScrollBehavior = topBarScrollBehavior,
                    bottomBarSelectedItem = bottomBarSelectedItem,
                    calendarViewMode = calendarViewMode,
                    onViewModeChanged = calendarViewModel::onViewModeChanged,
                    currentMonthAndYear = currentMonth,
                    onSelectedDateChanged = calendarViewModel::onSelectedDateChanged,
                    onTitleClicked = onTitleClicked,
                    onOpenFiltersClicked = {
                        isCalendarExpanded.value = false
                        isFilterExpanded.value = !isFilterExpanded.value
                    }
                )
            }
        },
        bottomBar = {
            if (isBottomBarVisible) {
                ToDoAppBottomBar(navController = navController)
            }
        }
    ) { padding ->
        val density = LocalDensity.current
        val anchorViewHeight = remember { mutableStateOf(342.dp) }
        val isNestedViewExpanded = remember {
            derivedStateOf {
                if (isCalendarExpanded.value) isCalendarExpanded.value else isFilterExpanded.value
            }
        }
        val onNestedViewClosed = {
            isCalendarExpanded.value = false
            isFilterExpanded.value = false
        }

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
                calendarViewModel = calendarViewModel,
                achievementsViewModel = achievementsViewModel,
                selectedDate = selectedDate,
                anchorViewHeight = anchorViewHeight,
                isNestedViewExpanded = isNestedViewExpanded,
                onNestedViewClosed = onNestedViewClosed,
                nestedContent = { modifier ->
                    val modifier = modifier
                        .onSizeChanged {
                            anchorViewHeight.value = with(density) { (it.height / density.density).dp }
                        }

                    when {
                        isCalendarExpanded.value -> {
                            CalendarView(
                                modifier = modifier,
                                isCalendarExpanded = isCalendarExpanded,
                                selectedDate = selectedDate,
                                onSelectedDateChanged = calendarViewModel::onSelectedDateChanged
                            )
                        }

                        isFilterExpanded.value -> {
                            FilterView(
                                modifier = modifier,
                                filterViewModel = filterViewModel,
                                isFilterExpanded = isFilterExpanded
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    isCalendarSelected: Boolean,
    bottomBarSelectedItem: TopLevelRoute<*>?,
    topBarScrollBehavior: TopAppBarScrollBehavior,
    calendarViewMode: State<CalendarViewMode>,
    onViewModeChanged: () -> Unit,
    currentMonthAndYear: State<Pair<Month, Int>>,
    onSelectedDateChanged: (LocalDate) -> Unit,
    onTitleClicked: () -> Unit,
    onOpenFiltersClicked: () -> Unit
) {
    val topBarTitle = bottomBarSelectedItem?.name?.let { stringResource(id = it) } ?: ""
    val topBarSubTitle = when {
        isCalendarSelected -> {
            val (month, year) = currentMonthAndYear.value
            val monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault()).lowercase()

            if (getNowDate().year == year) monthName else "$monthName $year"
        }

        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            // TODO Check shadow
            .shadow(elevation = 8.dp, spotColor = Color.Transparent)
    ) {
        ToDoAppTopBar(
            title = topBarTitle,
            onTitleClicked = if (isCalendarSelected) onTitleClicked else emptyLambda,
            subTitle = topBarSubTitle,
            scrollBehavior = topBarScrollBehavior,
            actions = {
                FiltersButtonsRow(
                    isCalendarSelected = isCalendarSelected,
                    onSelectedDateChanged = onSelectedDateChanged,
                    calendarViewMode = calendarViewMode,
                    onViewModeChanged = onViewModeChanged,
                    onOpenFiltersClicked = onOpenFiltersClicked
                )
            }
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