package com.apphico.todoapp.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.RecurringTask
import com.apphico.core_model.Reminder
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.designsystem.R
import com.apphico.extensions.add
import com.apphico.extensions.addDaysBetween
import com.apphico.extensions.addMinutesBetween
import com.apphico.extensions.ifTrue
import com.apphico.extensions.isEqualToBy
import com.apphico.extensions.remove
import com.apphico.extensions.update
import com.apphico.todoapp.group.GROUP_ARG
import com.apphico.todoapp.location.LOCATION_ARG
import com.apphico.todoapp.location.REMOVE_LOCATION_ARG
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val taskRepository: TaskRepository,
    private val checkListRepository: CheckListRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    private val addEditTaskParameters = savedStateHandle.toRoute<AddEditTaskRoute>(
        typeMap = mapOf(typeOf<AddEditTaskParameters>() to CustomNavType(AddEditTaskParameters::class.java, AddEditTaskParameters.serializer()))
    ).addEditTaskParameters

    private var task = addEditTaskParameters.task

    val editingTask = MutableStateFlow(task ?: Task())
    val editingCheckList = MutableStateFlow(task?.checkList ?: emptyList())

    val initialStartDate = task?.startDate
    val isEditing = task != null

    val nameError = MutableStateFlow<Int?>(null)
    val startDateError = MutableStateFlow<Int?>(null)

    init {
        if (addEditTaskParameters.isFromIntent) {
            viewModelScope.launch(Dispatchers.IO) {
                val dbTask = taskRepository.getTask(task!!.id)
                    .copy(startDate = task?.startDate, startTime = task?.startTime)

                task = dbTask
                editingTask.value = dbTask
                editingCheckList.value = dbTask.checkList
            }
        }

        viewModelScope.launch {
            savedStateHandle.getStateFlow<Group?>(GROUP_ARG, null)
                .filterNotNull()
                .map { editingTask.value.copy(group = it) }
                .flowOn(Dispatchers.IO)
                .collectLatest(editingTask::emit)
        }

        viewModelScope.launch {
            savedStateHandle.getStateFlow<Location?>(LOCATION_ARG, null)
                .filterNotNull()
                .map { locationArg ->
                    val location = editingTask.value.location?.id?.let { locationId ->
                        locationArg.copy(id = locationId)
                    } ?: locationArg

                    editingTask.value.copy(location = location)
                }
                .flowOn(Dispatchers.IO)
                .collectLatest(editingTask::emit)
        }

        viewModelScope.launch {
            savedStateHandle.getLiveData<Boolean>(REMOVE_LOCATION_ARG, false).asFlow()
                .filterNotNull()
                .ifTrue()
                .map { editingTask.value.copy(location = null) }
                .flowOn(Dispatchers.IO)
                .collectLatest(editingTask::emit)
        }
    }

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
        val newEndDate = editingTask.value.endDate?.addDaysBetween(editingTask.value.startDate, date)

        editingTask.value = editingTask.value.copy(startDate = date)
        editingTask.value = editingTask.value.copy(endDate = newEndDate)

        startDateError.value = null
    }

    fun onStartTimeChanged(time: LocalTime) {
        val (newEndDate, newEndTime) =
            Pair(
                editingTask.value.endDate,
                editingTask.value.endTime
            )
                .addMinutesBetween(
                    startDate = editingTask.value.startDate,
                    startTime = editingTask.value.startTime,
                    endTime = time
                )

        editingTask.value = editingTask.value.copy(startTime = time)
        newEndDate?.let { editingTask.value = editingTask.value.copy(endDate = it) }
        newEndTime?.let { editingTask.value = editingTask.value.copy(endTime = it) }

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

    fun onCheckListItemChanged(oldItem: CheckListItem, newItem: CheckListItem) {
        editingCheckList.value = editingCheckList.value.update(oldItem, newItem)
    }

    fun onCheckListItemItemAdded(checkListItem: CheckListItem) {
        editingCheckList.value = editingCheckList.value.add(checkListItem)
    }

    fun onCheckListItemItemRemoved(checkListItem: CheckListItem) {
        editingCheckList.value = editingCheckList.value.remove(checkListItem)
    }

    fun setCheckListItemDone(checkListItem: CheckListItem, taskDate: LocalDate?, isDone: Boolean) = viewModelScope.launch {
        if (checkListRepository.changeCheckListItemDone(checkListItem, taskDate, isDone)) {
            val newDoneDates = if (isDone) {
                checkListItem.doneDates + "${if (checkListItem.doneDates.isNullOrEmpty()) "" else ", "}, $taskDate"
            } else {
                checkListItem.doneDates?.replace(taskDate.toString(), "")
            }
            val newItem = checkListItem.copy(hasDone = isDone, doneDates = newDoneDates)

            editingCheckList.value = editingCheckList.value.update(checkListItem, newItem)
        }
    }

    fun onReminderChanged(reminder: Reminder?) {
        editingTask.value = editingTask.value.copy(reminder = reminder)
    }

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
            !editingTask.checkList.isEqualToBy(editingCheckList.value) { it.name } -> true
            editingTask.reminder != task.reminder -> true
            editingTask.location != task.location -> true
            else -> false
        }
    }

    fun save(
        saveMethod: RecurringTask,
        onResult: (Boolean) -> Unit
    ) {
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

        if ((task.startDate != null && task.startTime != null && task.endDate != null && task.endTime != null
                    && LocalDateTime.of(task.startDate, task.startTime) > LocalDateTime.of(task.endDate, task.endTime))
            || (task.startDate != null && task.endDate != null && task.startDate!! > task.endDate)
            || (task.startTime != null && task.endTime != null && task.startTime!! > task.endTime)
        ) {
            hasError = true
            startDateError.value = R.string.start_date_after_end_date_error_message
            return
        }

        if (!hasError) {
            task = task.copy(checkList = editingCheckList.value)

            viewModelScope.launch {
                onResult(
                    if (isEditing) {
                        taskRepository.updateTask(task, saveMethod, initialStartDate)
                    } else {
                        taskRepository.insertTask(task)
                    }
                )
            }
        }
    }

    fun delete(
        deleteMethod: RecurringTask,
        onResult: (Boolean) -> Unit
    ) {
        var task = editingTask.value

        viewModelScope.launch {
            onResult(taskRepository.deleteTask(task, deleteMethod))
        }
    }

    fun copy(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult(
                taskRepository.insertTask(editingTask.value.copy(id = 0))
            )
        }
    }
}
