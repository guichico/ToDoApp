package com.apphico.todoapp.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.achievements.AchievementRepository
import com.apphico.core_repository.calendar.settings.UserSettingsRepository
import com.apphico.extensions.addOrRemove
import com.apphico.extensions.startWith
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    val selectedStatus = userSettingsRepository.getTaskStatus()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, Status.ALL)

    val selectedGroups = MutableStateFlow(emptyList<Group>())

    val searchClicked = MutableSharedFlow<Boolean>()

    val achievements = searchClicked.startWith(true)
        .flatMapLatest {
            val status = selectedStatus.value
            val groups = selectedGroups.value

            achievementRepository.getAll(status, groups)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSelectedStatusChanged(status: Status) = viewModelScope.launch {
        userSettingsRepository.setTaskStatus(status)
    }

    fun onSelectedGroupChanged(group: Group) {
        selectedGroups.value = selectedGroups.value.addOrRemove(group)
    }
}