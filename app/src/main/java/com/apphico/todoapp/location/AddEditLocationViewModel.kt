package com.apphico.todoapp.location

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.todoapp.navigation.CustomNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val location = savedStateHandle.toRoute<AddEditLocationRoute>(
        typeMap = mapOf(
            typeOf<AddEditLocationParameters>() to CustomNavType(
                AddEditLocationParameters::class.java,
                AddEditLocationParameters.serializer()
            )
        )
    ).addEditLocationParameters.location

    val editingLocation = MutableStateFlow(location)

    fun onAddressTextChanged(text: String) {
        editingLocation.value = editingLocation.value.copy(address = text)
    }

    fun searchMyLocation() {
        viewModelScope.launch {
            locationRepository
                .getMyLocationFullAddress(context)
                .filterNotNull()
                .collect(editingLocation)
        }
    }

    fun searchLocation(text: String?) {
        if (!text.isNullOrEmpty()) {
            viewModelScope.launch {
                locationRepository
                    .getFromName(context, text)
                    .filterNotNull()
                    .collect(editingLocation)
            }
        }
    }
}
