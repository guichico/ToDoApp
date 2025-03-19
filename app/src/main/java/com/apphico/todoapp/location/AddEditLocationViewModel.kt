package com.apphico.todoapp.location

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.Coordinates
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    private val location = savedStateHandle.toRoute<AddEditLocationRoute>(
        typeMap = mapOf(
            typeOf<AddEditLocationParameters>() to CustomNavType(
                AddEditLocationParameters::class.java,
                AddEditLocationParameters.serializer()
            )
        )
    ).addEditLocationParameters.location

    val editingLocation = MutableStateFlow(location)
    val editingAddress = MutableStateFlow(location?.address)

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Coordinates?>(COORDINATES_ARG, null)
                .mapNotNull { it }
                .collectLatest { coordinates ->
                    searchFromCoordinates(coordinates)
                }
        }

        viewModelScope.launch {
            editingLocation
                .map { it?.address }
                .collectLatest(editingAddress::emit)
        }
    }

    fun onAddressTextChanged(text: String) {
        editingAddress.value = text
    }

    fun setDefaultLocation() = viewModelScope.launch {
        if (editingLocation.value == null) {
            locationRepository.getMyLocationFullAddress(context)
                .flowOn(Dispatchers.IO)
                .collectLatest(editingLocation::emit)
        }
    }

    fun searchFromCoordinates(coordinates: Coordinates) = viewModelScope.launch {
        locationRepository.getFromCoordinates(context, coordinates)
            .flowOn(Dispatchers.IO)
            .collectLatest(editingLocation::emit)
    }

    fun searchLocation(text: String?) = viewModelScope.launch {
        if (!text.isNullOrEmpty()) {
            locationRepository.getFromName(context, text)
                .filterNotNull()
                .collectLatest(editingLocation::emit)
        }
    }
}
