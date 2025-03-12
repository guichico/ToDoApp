package com.apphico.todoapp.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.extensions.ifTrue
import com.apphico.extensions.startWith
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import com.apphico.todoapp.task.SHOULD_REFRESH_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    calendarRepository: CalendarRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    private val shouldRefresh = savedStateHandle.getLiveData<Boolean>(SHOULD_REFRESH_ARG, false).asFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendar = shouldRefresh
        .startWith(true)
        .ifTrue()
        .flatMapLatest { calendarRepository.getCalendar() }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}