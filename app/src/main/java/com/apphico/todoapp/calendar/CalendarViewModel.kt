package com.apphico.todoapp.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.core_repository.calendar.datastore.AppSettingsDataStore
import com.apphico.core_repository.calendar.settings.UserSettingsRepository
import com.apphico.extensions.addOrRemove
import com.apphico.extensions.combine
import com.apphico.extensions.getNowDate
import com.apphico.extensions.startWith
import com.apphico.todoapp.FilterViewModel
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appSettingsDataStore: AppSettingsDataStore,
    private val userSettingsRepository: UserSettingsRepository,
    private val calendarRepository: CalendarRepository,
    private val checkListRepository: CheckListRepository
) : SavedStateHandleViewModel(savedStateHandle), FilterViewModel {

    val wasWelcomeClosed = appSettingsDataStore.wasWelcomeClosed

    val currentMonth = MutableStateFlow<Pair<Month, Int>>(with(getNowDate()) { month to year })

    val selectedDate = MutableStateFlow(LocalDate.now())
    override val selectedGroups = MutableStateFlow(emptyList<Group>())

    val calendarViewMode = userSettingsRepository.getViewMode()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, CalendarViewMode.DAY)

    override val selectedStatus = userSettingsRepository.getTaskStatus()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Status.ALL)

    override val searchClicked = MutableSharedFlow<Boolean>()

    val calendar = combine(
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
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setWasWelcomeClosed() = viewModelScope.launch { appSettingsDataStore.setWasWelcomeClosed(true) }

    fun onCurrentMonthChanged(month: Month?, year: Int?) {
        if (month != null && year != null) currentMonth.value = Pair(month, year)
    }

    fun onViewModeChanged() = viewModelScope.launch {
        userSettingsRepository.setViewMode(
            when (calendarViewMode.value) {
                CalendarViewMode.DAY -> CalendarViewMode.AGENDA
                CalendarViewMode.AGENDA -> CalendarViewMode.DAY
            }
        )
    }

    fun onSelectedDateChanged(date: LocalDate) {
        selectedDate.value = date
    }

    override fun onSelectedStatusChanged(status: Status) {
        viewModelScope.launch {
            userSettingsRepository.setTaskStatus(status)
        }
    }

    override fun onSelectedGroupChanged(group: Group) {
        selectedGroups.value = selectedGroups.value.addOrRemove(group)
    }

    override fun onSearchClicked() {
        viewModelScope.launch {
            searchClicked.emit(true)
        }
    }

    fun setTaskDone(task: Task, isDone: Boolean) = viewModelScope.launch {
        calendarRepository.changeTaskDone(task, isDone)
    }

    fun setCheckListItemDone(checkListItem: CheckListItem, task: Task, isDone: Boolean) = viewModelScope.launch {
        checkListRepository.changeCheckListItemDone(checkListItem, task.startDate, isDone)
    }
}
