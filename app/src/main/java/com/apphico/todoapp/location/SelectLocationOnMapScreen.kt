package com.apphico.todoapp.location

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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.Coordinates
import com.apphico.designsystem.R
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.map.FullScreenMapView
import com.apphico.designsystem.theme.ToDoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationOnMapScreen(
    selectLocationOnMapViewModel: SelectLocationOnMapViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateBackToAddEditLocation: (Coordinates?) -> Unit
) {
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
            coordinates = remember { mutableStateOf(selectLocationOnMapViewModel.coordinates) },
            onConfirmLocationClicked = navigateBackToAddEditLocation
        )
    }
}


@Composable
private fun SelectLocationOnMapScreenContent(
    innerPadding: PaddingValues,
    coordinates: State<Coordinates?>,
    onConfirmLocationClicked: (Coordinates?) -> Unit
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
                coordinates = coordinates,
                onConfirmLocationClicked = onConfirmLocationClicked
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SelectLocationOnMapScreenPreview() {
    ToDoAppTheme {
        SelectLocationOnMapScreenContent(
            innerPadding = PaddingValues(),
            coordinates = remember { mutableStateOf(null) },
            onConfirmLocationClicked = {}
        )
    }
}