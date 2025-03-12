package com.apphico.todoapp.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import com.apphico.todoapp.group.GROUP_ARG
import com.apphico.todoapp.location.LOCATION_ARG
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.OnCompleteListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val group = savedStateHandle.getStateFlow<Group?>(GROUP_ARG, null)
    private val location = savedStateHandle.getStateFlow<Location?>(LOCATION_ARG, null)

    val editingTask = MutableStateFlow(task ?: Task())
    val isEditing = task != null

    val startTime = MutableStateFlow(task?.startDate?.toLocalTime())
    val endTime = MutableStateFlow(task?.endDate?.toLocalTime())

    init {
        viewModelScope.launch {
            group
                .mapNotNull { it }
                .collect { g ->
                    editingTask.value = editingTask.value.copy(group = g)
                }

            location
                .mapNotNull { it }
                .collect { l ->
                    editingTask.value = editingTask.value.copy(location = l)
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

    fun delete(onCompleteListener: OnCompleteListener) {
        var task = editingTask.value

        viewModelScope
            .launch {
                val isSuccess = taskRepository
                    .deleteTask(task)

                if (isSuccess) onCompleteListener.onSuccess() else onCompleteListener.onError()
            }
    }

    fun save(onCompleteListener: OnCompleteListener) {
        editingTask.value.let { task ->
            if (task.name.isEmpty()) {
                return
            }
        }

        var task = editingTask.value

        startTime.value?.let { startTime ->
            task = task.copy(startDate = task.startDate?.withHour(startTime.hour)?.withMinute(startTime.minute))
        }
        endTime.value?.let { endTime ->
            task = task.copy(endDate = task.endDate?.withHour(endTime.hour)?.withMinute(endTime.minute))
        }

        viewModelScope
            .launch {
                val isSuccess = if (isEditing) {
                    taskRepository
                        .updateTask(task)
                } else {
                    taskRepository
                        .insertTask(task)
                }

                if (isSuccess) onCompleteListener.onSuccess() else onCompleteListener.onError()
            }
    }
}



