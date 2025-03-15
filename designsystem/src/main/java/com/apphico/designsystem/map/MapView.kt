package com.apphico.designsystem.map

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.core_model.Location
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
    location: State<Location?>,
    onAddressChanged: (String) -> Unit,
    onSearchLocationClicked: (String?) -> Unit,
    onEditLocationClicked: (Location?) -> Unit
) {
    val locationUpdates = remember { mutableStateOf(location.value?.coordinates) }

    Column(
        modifier = modifier
    ) {
        AddressField(
            location = location,
            onAddressChanged = onAddressChanged,
            onSearchLocationClicked = onSearchLocationClicked
        )
        Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.extraLarge))
        Surface(
            shape = CardDefaults.shape
        ) {
            GMap(
                location = location,
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
                    onClick = { onEditLocationClicked(location.value) }
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
    location: State<Location?>,
    onAddressChanged: (String) -> Unit,
    onSearchLocationClicked: (String?) -> Unit
) {
    val locationAddress by remember { derivedStateOf { location.value?.address } }

    LaunchedEffect(locationAddress) {
        locationAddress?.let { onAddressChanged(it) }
    }

    NormalTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = location.value?.address ?: "",
        placeholder = stringResource(id = R.string.address),
        onValueChange = { onAddressChanged(it) },
        textStyle = MaterialTheme.typography.titleMedium,
        trailingIcon = {
            ToDoAppIconButton(
                icon = ToDoAppIcons.icSearch,
                onClick = { onSearchLocationClicked(location.value?.address) }
            )
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchLocationClicked(location.value?.address) }),
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun MapViewPreview(
) {
    ToDoAppTheme {
        MapView(
            modifier = Modifier.fillMaxSize(),
            location = remember { mutableStateOf(null) },
            onAddressChanged = {},
            onSearchLocationClicked = {},
            onEditLocationClicked = {}
        )
    }
}