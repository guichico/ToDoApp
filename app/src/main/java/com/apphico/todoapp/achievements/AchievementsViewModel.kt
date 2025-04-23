package com.apphico.todoapp.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.achievements.AchievementRepository
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.core_repository.calendar.settings.UserSettingsRepository
import com.apphico.extensions.addOrRemove
import com.apphico.extensions.startWith
import com.apphico.todoapp.FilterViewModel
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val achievementRepository: AchievementRepository,
    private val checkListRepository: CheckListRepository
) : ViewModel(), FilterViewModel {

    override val selectedStatus = userSettingsRepository.getAchievementStatus()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, Status.ALL)

    override val selectedGroups = MutableStateFlow(emptyList<Group>())

    override val searchClicked = MutableSharedFlow<Boolean>()

    val achievements = searchClicked.startWith(true)
        .flatMapLatest {
            val status = selectedStatus.value
            val groups = selectedGroups.value

            achievementRepository.getAll(status, groups)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override fun onSelectedStatusChanged(status: Status) {
        viewModelScope.launch {
            userSettingsRepository.setAchievementStatus(status)
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

    fun setCheckListItemDone(checkListItem: CheckListItem, parentDate: LocalDate?, isDone: Boolean) {
        viewModelScope.launch {
            checkListRepository.changeCheckListItemDone(checkListItem, parentDate, isDone)
        }
    }
}