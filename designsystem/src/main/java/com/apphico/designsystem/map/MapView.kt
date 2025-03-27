package com.apphico.designsystem.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Coordinates
import com.apphico.designsystem.R
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.components.icons.ToDoAppIconButton
import com.apphico.designsystem.components.textfield.NormalTextField
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    coordinates: State<Coordinates?>,
    address: State<String?>,
    onAddressChanged: (String) -> Unit,
    onSearchLocationClicked: (String?) -> Unit,
    onEditLocationClicked: (Coordinates?) -> Unit,
    isLoadingLocation: State<Boolean>
) {
    val locationUpdates = remember { mutableStateOf(coordinates.value) }

    Column(
        modifier = modifier
    ) {
        AddressField(
            address = address,
            onAddressChanged = onAddressChanged,
            onSearchLocationClicked = onSearchLocationClicked,
            isLoadingLocation = isLoadingLocation
        )
        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.extraLarge))
        Surface(
            shape = CardDefaults.shape
        ) {
            GMap(
                coordinates = coordinates,
                locationUpdates = locationUpdates,
                isControlsEnabled = false
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
                        containerColor = White
                    ),
                    onClick = { onEditLocationClicked(locationUpdates.value) }
                ) {
                    ToDoAppIcon(
                        modifier = Modifier
                            .padding(end = ToDoAppTheme.spacing.small),
                        icon = ToDoAppIcons.icEditLocation,
                        contentDescription = "edit location",
                        tint = Black
                    )
                    Text(
                        text = stringResource(R.string.select_location_on_map),
                        style = MaterialTheme.typography.titleSmall,
                        color = Black
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressField(
    address: State<String?>,
    onAddressChanged: (String) -> Unit,
    onSearchLocationClicked: (String?) -> Unit,
    isLoadingLocation: State<Boolean>
) {
    val locationAddress by remember { derivedStateOf { address.value } }

    LaunchedEffect(locationAddress) {
        locationAddress?.let { onAddressChanged(it) }
    }

    NormalTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = address.value ?: "",
        placeholder = stringResource(id = R.string.address),
        onValueChange = { onAddressChanged(it) },
        textStyle = MaterialTheme.typography.titleMedium,
        trailingIcon = {
            if (isLoadingLocation.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                ToDoAppIconButton(
                    icon = ToDoAppIcons.icSearch,
                    onClick = { onSearchLocationClicked(address.value) }
                )
            }
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchLocationClicked(address.value) }),
    )
}

@PreviewLightDark
@Composable
private fun MapViewPreview(
) {
    ToDoAppTheme {
        MapView(
            modifier = Modifier.fillMaxSize(),
            coordinates = remember { mutableStateOf(null) },
            address = remember { mutableStateOf(null) },
            onAddressChanged = {},
            onSearchLocationClicked = {},
            onEditLocationClicked = {},
            isLoadingLocation = remember { mutableStateOf(false) }
        )
    }
}