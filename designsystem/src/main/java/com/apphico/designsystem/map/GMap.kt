package com.apphico.designsystem.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Location
import com.apphico.core_model.toCoordinates
import com.apphico.core_model.toLatLng
import com.apphico.designsystem.theme.ChiliRed
import com.apphico.designsystem.theme.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

private const val ZOOM = 16f

@Composable
fun GMap(
    location: State<Location?>,
    locationUpdates: MutableState<Coordinates?>,
    isControlsEnabled: Boolean
) {
    val cameraPositionState = rememberCameraPositionState()

    val cameraPositionUpdates = remember { derivedStateOf { cameraPositionState.position.target.toCoordinates() } }

    LaunchedEffect(cameraPositionUpdates.value) {
        locationUpdates.value = cameraPositionUpdates.value
    }

    val locationCoordinates by remember { derivedStateOf { location.value?.coordinates } }

    LaunchedEffect(locationCoordinates) {
        locationCoordinates?.let {
            cameraPositionState.move(
                update = CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(it.toLatLng(), ZOOM))
            )
        }
    }

    val uiSettings = if (isControlsEnabled) {
        MapUiSettings()
    } else
        MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
        )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(mapType = MapType.NORMAL),
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ToDoAppIcon(
            modifier = Modifier.size(48.dp),
            icon = ToDoAppIcons.icLocationOn,
            contentDescription = "marker",
            tint = ChiliRed
        )
    }
}