package com.apphico.todoapp.task

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.todoapp.di.AddEditLocationScreenArgModule
import com.apphico.todoapp.di.AddEditTaskScreenArgModule
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AddEditLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @AddEditTaskScreenArgModule.TaskData val taskArg: Task?,
    @AddEditLocationScreenArgModule.LocationData private val locationArg: Location?,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val location = MutableStateFlow(locationArg)

    fun onAddressTextChanged(text: String) {
        location.value = location.value?.copy(address = text)
    }

    fun searchMyLocation() {
        viewModelScope.launch {
            locationRepository
                .getMyLocationFullAddress(context)
                .filterNotNull()
                .collect(location)
        }
    }

    fun searchLocation(text: String?) {
        if (!text.isNullOrEmpty()) {
            viewModelScope.launch {
                locationRepository
                    .getFromName(context, text)
                    .filterNotNull()
                    .collect(location)
            }
        }
    }
}
