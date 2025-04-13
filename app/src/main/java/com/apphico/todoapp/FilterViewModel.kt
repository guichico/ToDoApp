package com.apphico.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

interface FilterViewModel {
    val selectedStatus: StateFlow<Status>
    val selectedGroups: StateFlow<List<Group>>
    val searchClicked: SharedFlow<Boolean>

    fun onSelectedStatusChanged(status: Status)
    fun onSelectedGroupChanged(group: Group)
    fun onSearchClicked()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GroupViewModel @Inject constructor(
    groupRepository: GroupRepository
) : ViewModel() {

    val groups = groupRepository.getGroups()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}