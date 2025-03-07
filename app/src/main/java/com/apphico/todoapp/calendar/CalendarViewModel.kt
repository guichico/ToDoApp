package com.apphico.todoapp.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    calendarRepository: CalendarRepository
) : ViewModel() {

    val calendar = calendarRepository.getCalendar()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}