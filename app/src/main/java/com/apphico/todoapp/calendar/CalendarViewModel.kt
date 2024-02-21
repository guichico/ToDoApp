package com.apphico.todoapp.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    val calendar = calendarRepository.getCalendar()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}