package com.apphico.todoapp.task

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.designsystem.R
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.map.FullScreenMapView
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.Screen
import com.apphico.todoapp.navigation.navigateWithArgs

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterSelectLocationOnMap() =
    when (initialState.destination.route) {
        Screen.AddEditLocation.route -> slideInVertically(initialOffsetY = { it })
        else -> fadeIn()
    }


fun AnimatedContentTransitionScope<NavBackStackEntry>.exitSelectLocationOnMap() =
    when (targetState.destination.route) {
        Screen.AddEditLocation.route -> slideOutVertically(targetOffsetY = { it })
        else -> fadeOut()
    }

fun NavController.navigateToSelectLocationOnMap(
    task: Task?,
    location: Location?
) {
    navigateWithArgs(
        route = Screen.SelectLocationOnMap.route,
        args = bundleOf(
            TASK_ARG to task,
            LOCATION_ARG to location
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationOnMapScreen(
    selectLocationOnMapViewModel: SelectLocationOnMapViewModel = hiltViewModel(),
    navigateBackToAddEditLocation: (Task?, Location) -> Unit,
    navigateBack: () -> Unit
) {
    val task = selectLocationOnMapViewModel.taskArg
    val location = selectLocationOnMapViewModel.location.collectAsState()

    val isSearchFinished by selectLocationOnMapViewModel.locationSearchFinished.collectAsState()

    LaunchedEffect(isSearchFinished) {
        if (isSearchFinished) {
            navigateBackToAddEditLocation(task, location.value!!)
        }
    }

    Scaffold(
        modifier = Modifier
            .consumeWindowInsets(WindowInsets.systemBars),
        topBar = {
            ToDoAppTopBar(
                navigateBack = navigateBack,
                title = stringResource(R.string.select_location),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            )
        }
    ) { innerPadding ->
        SelectLocationOnMapScreenContent(
            innerPadding = innerPadding,
            location = location,
            onConfirmLocationClicked = selectLocationOnMapViewModel::searchCoordinates
        )
    }
}

@Composable
fun SelectLocationOnMapScreenContent(
    innerPadding: PaddingValues,
    location: State<Location?>,
    onConfirmLocationClicked: (Coordinates) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = ToDoAppTheme.spacing.extraLarge,
                    horizontal = ToDoAppTheme.spacing.large
                )
                .imePadding()
        ) {
            FullScreenMapView(
                location = location,
                onConfirmLocationClicked = onConfirmLocationClicked
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun SelectLocationOnMapScreenPreview(

) {
    ToDoAppTheme {
        SelectLocationOnMapScreenContent(
            innerPadding = PaddingValues(),
            location = remember { mutableStateOf(null) },
            onConfirmLocationClicked = {}
        )
    }
}