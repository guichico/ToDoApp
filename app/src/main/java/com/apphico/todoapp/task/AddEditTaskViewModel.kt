package com.apphico.todoapp.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.extensions.getNowDateTime
import com.apphico.todoapp.group.GROUP_ARG
import com.apphico.todoapp.location.LOCATION_ARG
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
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

    val startTime = MutableStateFlow(task?.startDate?.toLocalTime())
    val endTime = MutableStateFlow(task?.endDate?.toLocalTime())

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

    fun onStartDateChanged(date: LocalDateTime?) {
        editingTask.value = editingTask.value.copy(startDate = date)
    }

    fun onStartTimeChanged(time: LocalTime) {
        startTime.value = time
    }

    fun onEndDateChanged(date: LocalDateTime?) {
        editingTask.value = editingTask.value.copy(endDate = date)
    }

    fun onEndTimeChanged(time: LocalTime) {
        endTime.value = time
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

        startTime.value?.let { startTime ->
            val starDate = task.startDate ?: getNowDateTime()
            task = task.copy(startDate = starDate.withHour(startTime.hour)?.withMinute(startTime.minute))
        }
        endTime.value?.let { endTime ->
            val endDate = task.endDate ?: getNowDateTime()
            task = task.copy(endDate = endDate.withHour(endTime.hour)?.withMinute(endTime.minute))
        }

        if (task.endDate != null && task.startDate == null) {
            // If task has a end date so it should have a star date
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