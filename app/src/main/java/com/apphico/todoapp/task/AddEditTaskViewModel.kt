package com.apphico.todoapp.task

import androidx.lifecycle.ViewModel
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Location
import com.apphico.core_model.Task
import com.apphico.todoapp.di.AddEditLocationScreenArgModule
import com.apphico.todoapp.di.AddEditTaskScreenArgModule
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    @AddEditTaskScreenArgModule.TaskData private val taskArg: Task?,
    @AddEditLocationScreenArgModule.LocationData private val locationArg: Location?
) : ViewModel() {

    val editingTask = MutableStateFlow(taskArg ?: Task())

    val isEditing = taskArg != null

    val startTime = MutableStateFlow(taskArg?.startDate?.toLocalTime())
    val endTime = MutableStateFlow(taskArg?.endDate?.toLocalTime())

    init {
        locationArg?.let {
            editingTask.value = editingTask.value.copy(location = it)
        }
    }

    fun hasChanges(): Boolean {
        val task = taskArg ?: Task()
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

    fun save() {
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

        println(task)
    }
}

