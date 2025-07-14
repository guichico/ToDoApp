package com.apphico.todoapp.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.repository.focus.FocusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    focusRepository: FocusRepository
) : ViewModel() {

    val routines = focusRepository.getRoutines()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
