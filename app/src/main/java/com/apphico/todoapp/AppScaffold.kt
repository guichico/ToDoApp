package com.apphico.todoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.apphico.core_model.Group
import com.apphico.core_model.fakeData.mockedGroups
import com.apphico.designsystem.components.date.CalendarView
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.views.FilterView
import com.apphico.designsystem.views.Status
import com.apphico.extensions.formatDayOfWeekDate
import com.apphico.extensions.formatMediumDate
import com.apphico.extensions.getNowDate
import com.apphico.todoapp.navigation.BottomBarNavigationItem
import com.apphico.todoapp.navigation.Flow
import com.apphico.todoapp.navigation.ToDoAppBottomBar
import com.apphico.todoapp.navigation.mainGraph

@Composable
fun AppScaffold(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val bottomBarSelectedItem = navBackStackEntry?.destination?.bottomBarSelectedItem()
    var isBottomBarVisible by remember { mutableStateOf(true) }

    LaunchedEffect(bottomBarSelectedItem) {
        isBottomBarVisible = bottomBarSelectedItem != null || navBackStackEntry?.destination?.route == null
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(ToDoAppTheme.spacing.large),
                    shape = CardDefaults.shape
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                start = ToDoAppTheme.spacing.extraSmall,
                                bottom = ToDoAppTheme.spacing.extraSmall
                            ),
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        topBar = {
            if (isBottomBarVisible) {
                TopBar(
                    navBackStackEntry = navBackStackEntry,
                    bottomBarSelectedItem = bottomBarSelectedItem
                )
            }
        },
        bottomBar = {
            if (isBottomBarVisible) {
                ToDoAppBottomBar(
                    navController = navController,
                    items = BottomBarNavigationItem.entries.map { it }.toTypedArray()
                )
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .padding(padding),
            navController = navController,
            startDestination = Flow.Main.route
        ) {
            mainGraph(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navBackStackEntry: NavBackStackEntry?,
    bottomBarSelectedItem: BottomBarNavigationItem?
) {
    val isCalendarSelected = bottomBarSelectedItem == BottomBarNavigationItem.CALENDAR
    val isFocusSelected = bottomBarSelectedItem == BottomBarNavigationItem.FOCUS

    val selectedDate = remember { mutableStateOf(getNowDate()) }

    val topBarTitle = bottomBarSelectedItem?.label?.let { stringResource(id = it) } ?: ""
    val topBarSubTitle = when {
        isCalendarSelected -> {
            when (selectedDate.value.year) {
                getNowDate().year -> selectedDate.value.formatDayOfWeekDate()
                else -> selectedDate.value.formatMediumDate()
            }
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
                Row {
                    if (isCalendarSelected) {
                        IconButton(
                            onClick = { selectedDate.value = getNowDate() }
                        ) {
                            ToDoAppIcon(
                                icon = ToDoAppIcons.icToday,
                                contentDescription = "today"
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            isCalendarExpanded.value = false
                            isFilterExpanded.value = !isFilterExpanded.value
                        }
                    ) {
                        ToDoAppIcon(
                            icon = ToDoAppIcons.icFilter,
                            contentDescription = "filter"
                        )
                    }
                }
            }
        )
        CalendarView(
            isCalendarExpanded = isCalendarExpanded,
            selectedDate = selectedDate,
            onSelectedDateChanged = { selectedDate.value = it }
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

fun NavDestination?.bottomBarSelectedItem() =
    BottomBarNavigationItem.entries.firstOrNull { it.route == this?.route }

private fun List<Group>.addOrRemoveGroup(group: Group): List<Group> {
    return this.toMutableList().apply {
        if (this.contains(group)) {
            remove(group)
        } else {
            add(group)
        }
    }
}