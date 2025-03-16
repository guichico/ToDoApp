package com.apphico.todoapp.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.task.TaskRepository
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

    fun hasChanges(): Boolean {
        val task = task ?: Task()
        val editingTask = editingTask.value

        // TODO Implement others
        return when {
            editingTask.name != task.name -> true
            editingTask.description != task.description -> true
            editingTask.startDate != task.startDate -> true
            editingTask.endDate != task.endDate -> true
            editingTask.reminder != task.reminder -> true
            editingTask.isDone != task.isDone -> true
            else -> false
        }
    }

    fun onNameChanged(text: String) {
        editingTask.value = editingTask.value.copy(name = text)
    }

    fun onDescriptionChanged(text: String) {
        editingTask.value = editingTask.value.copy(description = text)
    }

    fun onGroupRemoved() {
        editingTask.value = editingTask.value.copy(group = null)
    }

    fun onStartDateChanged(date: LocalDate?) {
        editingTask.value = editingTask.value.copy(startDate = date)
    }

    fun onStartTimeChanged(time: LocalTime) {
        editingTask.value = editingTask.value.copy(startTime = time)
    }

    fun onEndDateChanged(date: LocalDate?) {
        editingTask.value = editingTask.value.copy(endDate = date)
    }

    fun onEndTimeChanged(time: LocalTime) {
        editingTask.value = editingTask.value.copy(endTime = time)
    }

    fun onDaysOfWeekChanged(daysOfWeek: List<Int>) {
        editingTask.value = editingTask.value.copy(daysOfWeek = daysOfWeek)
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

    fun save(onResult: (Boolean) -> Unit) {
        editingTask.value.let { task ->
            if (task.name.isEmpty()) {
                return
            }
        }

        var task = editingTask.value

        if (task.endDate != null && task.startDate == null) {
            // If task has a end date so it should have a star date
            println("error")
        }

        if (task.endTime != null && task.startTime == null) {
            // If task has a end time so it should have a star time
            println("error")
        }

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

    fun delete(onResult: (Boolean) -> Unit) {
        var task = editingTask.value

        viewModelScope.launch {
            onResult(taskRepository.deleteTask(task))
        }
    }
}