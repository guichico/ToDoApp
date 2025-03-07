package com.apphico.todoapp.task

import android.Manifest
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.designsystem.CheckPermissions
import com.apphico.designsystem.R
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.map.MapView
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.navigation.Screen
import com.apphico.todoapp.navigation.navigateWithArgs

internal const val LOCATION_ARG = "location"

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterAddEditLocation() =
    when (initialState.destination.route) {
        Screen.AddEditTask.route -> slideInVertically(initialOffsetY = { it })
        else -> fadeIn()
    }


fun AnimatedContentTransitionScope<NavBackStackEntry>.exitAddEditLocation() =
    when (targetState.destination.route) {
        Screen.AddEditTask.route -> slideOutVertically(targetOffsetY = { it })
        else -> fadeOut()
    }

fun NavController.navigateToAddEditLocation(
    task: Task,
    location: Location?
) {
    navigateWithArgs(
        route = Screen.AddEditLocation.route,
        args = bundleOf(
            TASK_ARG to task,
            LOCATION_ARG to location
        ),
        navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
    )
}

fun NavController.navigateBackToAddEditLocation(
    task: Task?,
    location: Location?
) {
    navigateWithArgs(
        route = Screen.AddEditLocation.route,
        args = bundleOf(
            TASK_ARG to task,
            LOCATION_ARG to location
        ),
        navOptions = NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(Screen.AddEditLocation.route, true).build()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditLocationScreen(
    addEditLocationViewModel: AddEditLocationViewModel = hiltViewModel(),
    navigateToSelectLocationOnMap: (Task?, Location?) -> Unit,
    onConfirmClicked: (Task?, Location?) -> Unit,
    navigateBack: () -> Unit
) {
    val task = addEditLocationViewModel.taskArg
    val location = addEditLocationViewModel.location.collectAsState()

    val permissionsGranted = remember { mutableStateOf(false) }

    val locationPermissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    CheckPermissions(
        permissions = locationPermissions,
        onPermissionGrantedChanged = { permissionsGranted.value = it }
    )

    LaunchedEffect(permissionsGranted.value) {
        if (permissionsGranted.value && location.value == null) {
            addEditLocationViewModel.searchMyLocation()
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
        AddEditLocationScreenContent(
            innerPadding = innerPadding,
            task = task,
            location = location,
            onAddressChanged = addEditLocationViewModel::onAddressTextChanged,
            onSearchLocationClicked = addEditLocationViewModel::searchLocation,
            onConfirmClicked = onConfirmClicked,
            navigateToSelectLocationOnMap = navigateToSelectLocationOnMap,
            navigateBack = navigateBack
        )
    }
}

@Composable
private fun AddEditLocationScreenContent(
    innerPadding: PaddingValues,
    task: Task?,
    location: State<Location?>,
    onAddressChanged: (String) -> Unit,
    onSearchLocationClicked: (String?) -> Unit,
    onConfirmClicked: (Task?, Location?) -> Unit,
    navigateToSelectLocationOnMap: (Task?, Location?) -> Unit,
    navigateBack: () -> Unit
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
            MapView(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = ToDoAppTheme.spacing.extraLarge),
                task = task,
                location = location,
                onAddressChanged = onAddressChanged,
                onSearchLocationClicked = onSearchLocationClicked,
                onEditLocationClicked = navigateToSelectLocationOnMap
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NormalButton(
                    text = stringResource(R.string.cancel),
                    onClick = navigateBack
                )
                NormalButton(
                    text = stringResource(R.string.confirm),
                    onClick = { onConfirmClicked(task, location.value) }
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun AddEditLocationScreenPreview() {
    ToDoAppTheme {
        AddEditLocationScreenContent(
            innerPadding = PaddingValues(),
            task = Task(),
            location = remember { mutableStateOf(null) },
            onAddressChanged = {},
            onSearchLocationClicked = {},
            onConfirmClicked = { _, _ -> },
            navigateToSelectLocationOnMap = { _, _ -> },
            navigateBack = {}
        )
    }
}