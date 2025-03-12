package com.apphico.todoapp.location

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.Coordinates
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.todoapp.navigation.CustomNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class SelectLocationOnMapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val location = MutableStateFlow(
        savedStateHandle.toRoute<SelectLocationOnMapRoute>(
            typeMap = mapOf(
                typeOf<SelectLocationOnMapParameters>() to CustomNavType(
                    SelectLocationOnMapParameters::class.java,
                    SelectLocationOnMapParameters.serializer()
                )
            )
        ).selectLocationOnMapParameters.location
    )

    val isLocationSearchFinished = MutableStateFlow(false)

    fun searchCoordinates(coordinates: Coordinates) {
        viewModelScope.launch {
            locationRepository.getFromCoordinates(context, coordinates)
                .filterNotNull()
                .collectLatest {
                    location.value = it
                    isLocationSearchFinished.value = true
                }
        }
    }
}
