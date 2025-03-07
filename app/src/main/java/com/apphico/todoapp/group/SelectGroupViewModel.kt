package com.apphico.todoapp.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_repository.calendar.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor(
    groupRepository: GroupRepository
) : ViewModel() {

    val groups = groupRepository.getGroups()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}