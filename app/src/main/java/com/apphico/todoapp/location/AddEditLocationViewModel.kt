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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditLocationViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
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

    val isLoadingDefaultLocation = MutableStateFlow(true)

    init {
        savedStateHandle.getStateFlow<Coordinates?>(COORDINATES_ARG, null)
            .mapNotNull { it }
            .flatMapLatest { coordinates ->
                locationRepository.getFromCoordinates(context, coordinates)
            }
            .onEach(editingLocation::emit)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

        editingLocation
            .map { it?.address }
            .onEach { address ->
                if (address?.isNotEmpty() == true) isLoadingDefaultLocation.emit(false)
                editingAddress.emit(address)
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onAddressTextChanged(text: String) {
        editingAddress.value = text
    }

    fun setDefaultLocation() {
        if (editingLocation.value == null) {
            locationRepository.getLastKnownLocation(context)
                .onEach(editingLocation::emit)
                .flowOn(Dispatchers.IO)
                .launchIn(viewModelScope)

            locationRepository.getMyLocationFullAddress(context)
                .onEach { location ->
                    editingLocation.emit(location)
                    isLoadingDefaultLocation.emit(false)
                }
                .flowOn(Dispatchers.IO)
                .launchIn(viewModelScope)
        }
    }

    fun searchLocation(text: String?) = viewModelScope.launch {
        if (!text.isNullOrEmpty()) {
            locationRepository.getFromName(context, text)
                .filterNotNull()
                .flowOn(Dispatchers.IO)
                .collectLatest(editingLocation::emit)
        }
    }
}
