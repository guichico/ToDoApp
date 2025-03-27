package com.apphico.todoapp.location

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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Location
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.components.topbar.ToDoAppTopBar
import com.apphico.designsystem.location.CheckLocationPermission
import com.apphico.designsystem.map.MapView
import com.apphico.designsystem.theme.ToDoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditLocationScreen(
    addEditLocationViewModel: AddEditLocationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToSelectLocationOnMap: (Coordinates?) -> Unit,
    onConfirmClicked: (Location?) -> Unit,
    onRemoveClicked: () -> Unit,
) {
    val location = addEditLocationViewModel.editingLocation.collectAsState()
    val address = addEditLocationViewModel.editingAddress.collectAsState()

    val isLoadingDefaultLocation = addEditLocationViewModel.isLoadingDefaultLocation.collectAsState()

    CheckLocationPermission(
        navigateBack = navigateBack,
        onLocationPermissionGranted = addEditLocationViewModel::setDefaultLocation
    )

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
            location = location,
            address = address,
            onAddressChanged = addEditLocationViewModel::onAddressTextChanged,
            onSearchLocationClicked = addEditLocationViewModel::searchLocation,
            navigateToSelectLocationOnMap = navigateToSelectLocationOnMap,
            onConfirmClicked = onConfirmClicked,
            onRemoveClicked = onRemoveClicked,
            isLoadingDefaultLocation = isLoadingDefaultLocation
        )
    }
}

@Composable
private fun AddEditLocationScreenContent(
    innerPadding: PaddingValues,
    location: State<Location?>,
    address: State<String?>,
    onAddressChanged: (String) -> Unit,
    onSearchLocationClicked: (String?) -> Unit,
    navigateToSelectLocationOnMap: (Coordinates?) -> Unit,
    onConfirmClicked: (Location?) -> Unit,
    onRemoveClicked: () -> Unit,
    isLoadingDefaultLocation: State<Boolean>
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
                coordinates = remember { derivedStateOf { location.value?.coordinates } },
                address = address,
                onAddressChanged = onAddressChanged,
                onSearchLocationClicked = onSearchLocationClicked,
                onEditLocationClicked = navigateToSelectLocationOnMap,
                isLoadingLocation = isLoadingDefaultLocation
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NormalButton(
                    text = stringResource(R.string.remove),
                    onClick = onRemoveClicked
                )
                NormalButton(
                    text = stringResource(R.string.confirm),
                    onClick = { onConfirmClicked(location.value) }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AddEditLocationScreenPreview() {
    ToDoAppTheme {
        AddEditLocationScreenContent(
            innerPadding = PaddingValues(),
            location = remember { mutableStateOf(null) },
            address = remember { mutableStateOf(null) },
            onAddressChanged = {},
            onSearchLocationClicked = {},
            navigateToSelectLocationOnMap = {},
            onConfirmClicked = {},
            onRemoveClicked = {},
            isLoadingDefaultLocation = remember { mutableStateOf(false) }
        )
    }
}