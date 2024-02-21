package com.apphico.todoapp.task

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.todoapp.di.AddEditLocationScreenArgModule
import com.apphico.todoapp.di.AddEditTaskScreenArgModule
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SelectLocationOnMapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @AddEditTaskScreenArgModule.TaskData val taskArg: Task?,
    @AddEditLocationScreenArgModule.LocationData private val locationArg: Location?,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val location = MutableStateFlow(locationArg)

    val locationSearchFinished = MutableStateFlow(false)

    fun searchCoordinates(coordinates: Coordinates) {
        viewModelScope.launch {
            locationRepository.getFromCoordinates(context, coordinates)
                .filterNotNull()
                .collectLatest {
                    location.value = it
                    locationSearchFinished.value = true
                }
        }
    }
}
