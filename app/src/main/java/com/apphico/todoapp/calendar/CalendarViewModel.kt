package com.apphico.todoapp.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Task
import com.apphico.core_model.TaskStatus
import com.apphico.core_repository.calendar.CheckListRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.group.GroupRepository
import com.apphico.extensions.addOrRemove
import com.apphico.extensions.combine
import com.apphico.extensions.startWith
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

enum class CalendarViewMode { AGENDA, DAY }

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository,
    groupRepository: GroupRepository,
    private val checkListRepository: CheckListRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    val groups = groupRepository.getGroups()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val calendarViewMode = MutableStateFlow(CalendarViewMode.DAY)
    val selectedDate = MutableStateFlow(LocalDate.now())
    val selectedStatus = MutableStateFlow(TaskStatus.ALL)
    val selectedGroups = MutableStateFlow(emptyList<Group>())

    val calendar = MutableStateFlow(emptyList<Task>())

    val searchClicked = MutableSharedFlow<Boolean>()
    val isSearchFinished = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            combine(
                calendarViewMode,
                selectedDate,
                searchClicked.startWith(true)
            )
                .flatMapLatest { (viewMode, selectedDate) ->
                    val status = selectedStatus.value
                    val groups = selectedGroups.value

                    with(calendarRepository) {
                        when (viewMode) {
                            CalendarViewMode.DAY -> getFromDay(date = selectedDate, status = status, groups = groups)
                            CalendarViewMode.AGENDA -> getAll(fromStartDate = selectedDate, status = status, groups = groups)
                        }
                    }
                }
                .flowOn(Dispatchers.IO)
                .collectLatest { tasks ->
                    calendar.emit(tasks)
                    isSearchFinished.emit(true)
                }
        }
    }

    fun onScrollFinished() = viewModelScope.launch {
        isSearchFinished.emit(false)
    }

    fun onViewModeChanged() {
        calendarViewMode.value = when (calendarViewMode.value) {
            CalendarViewMode.DAY -> CalendarViewMode.AGENDA
            CalendarViewMode.AGENDA -> CalendarViewMode.DAY
        }
    }

    fun onSelectedDateChanged(date: LocalDate) {
        selectedDate.value = date
    }

    fun onSelectedStatusChanged(status: TaskStatus) {
        selectedStatus.value = status
    }

    fun onSelectedGroupChanged(group: Group) {
        selectedGroups.value = selectedGroups.value.addOrRemove(group)
    }

    fun onSearchClicked() = viewModelScope.launch {
        searchClicked.emit(true)
    }

    fun setTaskDone(task: Task, isDone: Boolean) = viewModelScope.launch {
        if (calendarRepository.changeTaskDone(task, isDone)) onSearchClicked()
    }

    fun setCheckListItemDone(checkListItem: CheckListItem, task: Task, isDone: Boolean) = viewModelScope.launch {
        if (checkListRepository.changeCheckListItemDone(checkListItem, task.startDate, isDone)) onSearchClicked()
    }
}