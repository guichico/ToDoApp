package com.apphico.todoapp.achievements

import androidx.lifecycle.ViewModel
import com.apphico.core_model.MeasurementType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddEditProgressViewModel @Inject constructor(

) : ViewModel() {

    val editingProgress = MutableStateFlow(MeasurementType.Percentage.PercentageProgress())

    // val isEditing = progressArg != null
    val isEditing = false

    fun hasChanges(): Boolean {
        return false
    }

    fun onProgressChanged(progress: Float) {
        editingProgress.value = editingProgress.value.copy(progress = progress)
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