package com.apphico.todoapp.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_repository.calendar.focus.FocusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val focusRepository: FocusRepository
) : ViewModel() {

    val routines = focusRepository.getRoutines()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        println("Init FocusViewModel")
    }

}
