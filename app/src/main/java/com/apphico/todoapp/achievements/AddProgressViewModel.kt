package com.apphico.todoapp.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.apphico.core_model.Progress
import com.apphico.todoapp.navigation.CustomNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditProgressViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val measurementType = savedStateHandle.toRoute<AddEditProgressRoute>(
        typeMap = mapOf(
            typeOf<AddEditProgressParameters>() to CustomNavType(
                AddEditProgressParameters::class.java,
                AddEditProgressParameters.serializer()
            )
        )
    ).addEditProgressParameters.measurementType

    val editingProgress = MutableStateFlow(Progress())

    // val isEditing = progressArg != null
    val isEditing = false

    fun hasChanges(): Boolean {
        return false
    }

    fun onProgressChanged(progress: Float) {
        editingProgress.value = editingProgress.value.copy(progress = (progress / 100))
    }

    fun onDescriptionChanged(text: String) {
        editingProgress.value = editingProgress.value.copy(description = text)
    }

    fun onDateChanged(date: LocalDate?) {
        editingProgress.value = editingProgress.value.copy(date = date)
    }

    fun onTimeChanged(time: LocalTime) {
        editingProgress.value = editingProgress.value.copy(time = time)
    }
}