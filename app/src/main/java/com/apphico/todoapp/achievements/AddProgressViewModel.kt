package com.apphico.todoapp.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.apphico.core_model.Progress
import com.apphico.designsystem.R
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

    private val addEditProgressParameters = savedStateHandle.toRoute<AddEditProgressRoute>(
        typeMap = mapOf(
            typeOf<AddEditProgressParameters>() to CustomNavType(
                AddEditProgressParameters::class.java,
                AddEditProgressParameters.serializer()
            )
        )
    ).addEditProgressParameters

    private val progressArg = addEditProgressParameters.progress
    val editingProgress = MutableStateFlow(progressArg ?: Progress())

    val measurementType = addEditProgressParameters.measurementType
    val measurementUnit = addEditProgressParameters.measurementUnit

    val isEditing = progressArg != null

    val progressError = MutableStateFlow<Int?>(null)

    fun hasChanges(): Boolean {
        val progress = progressArg ?: Progress()
        val editingProgress = editingProgress.value

        return when {
            editingProgress.progress != progress.progress -> true
            editingProgress.description != progress.description -> true
            editingProgress.date != progress.date -> true
            editingProgress.time != progress.time -> true
            else -> false
        }
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

    fun save(onResult: (Operation) -> Unit) {
        var progress = editingProgress.value
        var hasError = false

        editingProgress.value

        if (progress.progress <= 0) {
            hasError = true
            progressError.value = R.string.progress_error_message
        }

        if (!hasError) {
            onResult(if (isEditing) Operation.Update(progressArg!!, progress) else Operation.Save(progress))
        }
    }

    fun delete(onResult: (Operation) -> Unit) {
        onResult(Operation.Delete(editingProgress.value))
    }
}