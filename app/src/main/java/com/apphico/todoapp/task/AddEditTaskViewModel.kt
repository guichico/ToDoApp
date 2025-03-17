package com.apphico.todoapp.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.designsystem.R
import com.apphico.todoapp.group.GROUP_ARG
import com.apphico.todoapp.location.LOCATION_ARG
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val taskRepository: TaskRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    val task = savedStateHandle.toRoute<AddEditTaskRoute>(
        typeMap = mapOf(typeOf<AddEditTaskParameters>() to CustomNavType(AddEditTaskParameters::class.java, AddEditTaskParameters.serializer()))
    ).addEditTaskParameters.task

    val editingTask = MutableStateFlow(task ?: Task())
    val isEditing = task != null

    val nameError = MutableStateFlow<Int?>(null)
    val startDateError = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Group?>(GROUP_ARG, null)
                .mapNotNull { it }
                .collectLatest { g ->
                    editingTask.value = editingTask.value.copy(group = g)
                }
        }

        viewModelScope.launch {
            savedStateHandle.getStateFlow<Location?>(LOCATION_ARG, null)
                .mapNotNull { it }
                .collectLatest { locationArg ->
                    val location = editingTask.value.location?.id?.let { locationId ->
                        locationArg.copy(id = locationId)
                    } ?: locationArg

                    editingTask.value = editingTask.value.copy(location = location)
                }
        }
    }

    // region Fields Changed
    fun onNameChanged(text: String) {
        editingTask.value = editingTask.value.copy(name = text)
        nameError.value = null
    }

    fun onDescriptionChanged(text: String) {
        editingTask.value = editingTask.value.copy(description = text)
    }

    fun onGroupRemoved() {
        editingTask.value = editingTask.value.copy(group = null)
    }

    fun onStartDateChanged(date: LocalDate?) {
        editingTask.value = editingTask.value.copy(startDate = date)
        startDateError.value = null
    }

    fun onStartTimeChanged(time: LocalTime) {
        editingTask.value = editingTask.value.copy(startTime = time)
        startDateError.value = null
    }

    fun onEndDateChanged(date: LocalDate?) {
        editingTask.value = editingTask.value.copy(endDate = date)
        startDateError.value = null
    }

    fun onEndTimeChanged(time: LocalTime) {
        editingTask.value = editingTask.value.copy(endTime = time)
        startDateError.value = null
    }

    fun onDaysOfWeekChanged(daysOfWeek: List<Int>) {
        editingTask.value = editingTask.value.copy(daysOfWeek = daysOfWeek.sorted())
    }

    fun onCheckListChanged(checkList: List<CheckListItem>) {
        editingTask.value = editingTask.value.copy(checkList = checkList)
    }

    fun onReminderTimeChanged(time: LocalTime?) {
        editingTask.value = editingTask.value.copy(reminder = time)
    }

    fun onLocationRemoved() {
        editingTask.value = editingTask.value.copy(location = null)
    }
    // endregion

    fun hasChanges(): Boolean {
        val task = task ?: Task()
        val editingTask = editingTask.value

        return when {
            editingTask.name != task.name -> true
            editingTask.description != task.description -> true
            editingTask.group != task.group -> true
            editingTask.startDate != task.startDate -> true
            editingTask.startTime != task.startTime -> true
            editingTask.endDate != task.endDate -> true
            editingTask.endTime != task.endTime -> true
            editingTask.daysOfWeek != task.daysOfWeek -> true
            editingTask.checkList != task.checkList -> true
            editingTask.reminder != task.reminder -> true
            editingTask.location != task.location -> true
            editingTask.isDone != task.isDone -> true
            else -> false
        }
    }

    fun save(onResult: (Boolean) -> Unit) {
        var task = editingTask.value
        var hasError = false

        if (task.name.isEmpty()) {
            hasError = true
            nameError.value = R.string.name_error_message
        }

        if (task.daysOfWeek.isNotEmpty() && task.startDate == null) {
            hasError = true
            startDateError.value = R.string.day_of_week_should_have_start_date
            return
        }

        when {
            task.endTime != null && task.startTime == null -> {
                hasError = true
                startDateError.value = R.string.empty_start_time_error_message
                return
            }

            task.endDate != null && task.startDate == null -> {
                hasError = true
                startDateError.value = R.string.empty_start_date_error_message
                return
            }

            (task.startDate != null && task.startTime != null && task.endDate != null && task.endTime != null
                    && LocalDateTime.of(task.startDate, task.startTime) > LocalDateTime.of(task.endDate, task.endTime))
                    || (task.startDate != null && task.endDate != null && task.startDate!! > task.endDate)
                    || (task.startTime != null && task.endTime != null && task.startTime!! > task.endTime) -> {
                hasError = true
                startDateError.value = R.string.start_date_after_end_date_error_message
                return
            }
        }

        if (!hasError) {
            viewModelScope.launch {
                onResult(
                    if (isEditing) {
                        taskRepository.updateTask(task)
                    } else {
                        taskRepository.insertTask(task)
                    }
                )
            }
        }
    }

    fun delete(onResult: (Boolean) -> Unit) {
        var task = editingTask.value

        viewModelScope.launch {
            onResult(taskRepository.deleteTask(task))
        }
    }
}