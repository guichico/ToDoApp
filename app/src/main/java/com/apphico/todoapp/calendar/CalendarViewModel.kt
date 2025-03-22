package com.apphico.todoapp.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.CheckListRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.extensions.combine
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

enum class CalendarViewMode { AGENDA, DAY }

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository,
    private val checkListRepository: CheckListRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    private val calendarViewMode = MutableStateFlow(CalendarViewMode.DAY)
    private val selectedDate = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendar = combine(
        calendarViewMode,
        selectedDate
    )
        .flatMapLatest { (viewMode, selectedDate) ->
            with(calendarRepository) {
                when (viewMode) {
                    CalendarViewMode.DAY -> getFromDay(date = selectedDate)
                    CalendarViewMode.AGENDA -> getAll(fromStartDate = selectedDate)
                }
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setViewMode(viewMode: CalendarViewMode) {
        calendarViewMode.value = viewMode
    }

    fun setDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun setTaskDone(task: Task, isDone: Boolean) = viewModelScope.launch {
        calendarRepository.changeTaskDone(task, isDone)
    }

    fun setCheckListItemDone(checkListItem: CheckListItem, task: Task, isDone: Boolean) = viewModelScope.launch {
        checkListRepository.changeCheckListItemDone(checkListItem, task.startDate, isDone)
    }
}