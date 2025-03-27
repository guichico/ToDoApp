package com.apphico.designsystem.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Coordinates
import com.apphico.designsystem.R
import com.apphico.designsystem.theme.ChiliRed
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White

@Composable
fun FullScreenMapView(
    coordinates: State<Coordinates?>,
    onConfirmLocationClicked: (Coordinates?) -> Unit
) {
    val locationUpdates = remember { mutableStateOf(coordinates.value) }

    Surface(
        shape = CardDefaults.shape
    ) {
        GMap(
            coordinates = coordinates,
            locationUpdates = locationUpdates,
            isControlsEnabled = true
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = ToDoAppTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChiliRed
                ),
                onClick = {
                    onConfirmLocationClicked(locationUpdates.value)
                }
            ) {
                Text(
                    text = stringResource(R.string.confirm_location),
                    style = MaterialTheme.typography.titleSmall,
                    color = White
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun FullScreenMapViewPreview(

) {
    ToDoAppTheme {
        FullScreenMapView(
            coordinates = remember { mutableStateOf(null) },
            onConfirmLocationClicked = {}
        )
    }
}