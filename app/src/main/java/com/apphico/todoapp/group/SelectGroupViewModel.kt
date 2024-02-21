package com.apphico.todoapp.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_repository.calendar.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SelectGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val groups = groupRepository.getGroups()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


}