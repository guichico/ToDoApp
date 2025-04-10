package com.apphico.todoapp.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.core_repository.calendar.achievements.AchievementRepository
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.designsystem.R
import com.apphico.extensions.add
import com.apphico.extensions.isEqualToBy
import com.apphico.extensions.remove
import com.apphico.extensions.update
import com.apphico.todoapp.group.GROUP_ARG
import com.apphico.todoapp.navigation.CustomNavType
import com.apphico.todoapp.navigation.SavedStateHandleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AddEditAchievementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val achievementRepository: AchievementRepository,
    private val checkListRepository: CheckListRepository
) : SavedStateHandleViewModel(savedStateHandle) {

    private val achievement = savedStateHandle.toRoute<AddEditAchievementRoute>(
        typeMap = mapOf(
            typeOf<AddEditAchievementParameters>() to CustomNavType(
                AddEditAchievementParameters::class.java,
                AddEditAchievementParameters.serializer()
            )
        )
    ).addEditAchievementParameters.achievement

    val editingAchievement = MutableStateFlow(achievement ?: Achievement())
    val editingMeasurementType = MutableStateFlow(achievement?.measurementType)
    val editingCheckList = MutableStateFlow(achievement?.getCheckList() ?: emptyList())
    val progress = MutableStateFlow(achievement?.getProgress() ?: 0f)

    val isEditing = achievement != null

    val nameError = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Group?>(GROUP_ARG, null)
                .filterNotNull()
                .map { editingAchievement.value.copy(group = it) }
                .flowOn(Dispatchers.IO)
                .collectLatest(editingAchievement::emit)
        }
    }

    fun hasChanges(): Boolean {
        val achievement = achievement ?: Achievement()
        val editingAchievement = editingAchievement.value

        // TODO Implement others
        return when {
            editingAchievement.name != achievement.name -> true
            editingAchievement.description != achievement.description -> true
            editingAchievement.group != achievement.group -> true
            editingAchievement.endDate != achievement.endDate -> true
            editingAchievement.getCheckList().isEqualToBy(editingCheckList.value) { it.name } -> true
            editingAchievement.doneDate != achievement.doneDate -> true
            else -> false
        }
    }

    fun onNameChanged(text: String) {
        editingAchievement.value = editingAchievement.value.copy(name = text)
        nameError.value = null
    }

    fun onDescriptionChanged(text: String) {
        editingAchievement.value = editingAchievement.value.copy(description = text)
    }

    fun onGroupRemoved() {
        editingAchievement.value = editingAchievement.value.copy(group = null)
    }

    fun onEndDateChanged(date: LocalDate?) {
        editingAchievement.value = editingAchievement.value.copy(endDate = date)
    }

    fun onMeasurementTypeChanged(measurementType: MeasurementType) {
        editingAchievement.value = editingAchievement.value.copy(measurementType = measurementType)
        editingMeasurementType.value = measurementType
    }

    fun onCheckListItemChanged(oldItem: CheckListItem, newItem: CheckListItem) {
        editingCheckList.value = editingCheckList.value.update(oldItem, newItem)
    }

    fun onCheckListItemItemAdded(checkListItem: CheckListItem) {
        editingCheckList.value = editingCheckList.value.add(checkListItem)
    }

    fun onCheckListItemItemRemoved(checkListItem: CheckListItem) {
        editingCheckList.value = editingCheckList.value.remove(checkListItem)
    }

    fun setCheckListItemDone(checkListItem: CheckListItem, taskDate: LocalDate?, isDone: Boolean) = viewModelScope.launch {
        if (checkListRepository.changeCheckListItemDone(checkListItem, taskDate, isDone)) {
            val newDoneDates = if (isDone) {
                checkListItem.doneDates + "${if (checkListItem.doneDates.isNullOrEmpty()) "" else ", "}, $taskDate"
            } else {
                checkListItem.doneDates?.replace(taskDate.toString(), "")
            }
            val newItem = checkListItem.copy(hasDone = isDone, doneDates = newDoneDates)

            editingCheckList.value = editingCheckList.value.update(checkListItem, newItem)
        }
    }

    fun onUnitChanged(unit: MeasurementValueUnit) {
        editingMeasurementType.value = editingMeasurementType.value?.let {
            (it as MeasurementType.Value).copy(unit = unit)
        } ?: run {
            MeasurementType.Value(unit = unit, startingValue = 0f, goalValue = 0f)
        }
    }

    fun ondStartingValueChanged(startingValue: Float) {
        editingMeasurementType.value = editingMeasurementType.value?.let {
            (it as MeasurementType.Value).copy(startingValue = startingValue)
        } ?: run {
            MeasurementType.Value(startingValue = startingValue, goalValue = 0f)
        }
    }

    fun ondGoalValueChanged(goalValue: Float) {
        editingMeasurementType.value = editingMeasurementType.value?.let {
            (it as MeasurementType.Value).copy(goalValue = goalValue)
        } ?: run {
            MeasurementType.Value(startingValue = 0f, goalValue = goalValue)
        }
    }

    fun onTrackedValuesChanged(trackedValues: List<MeasurementType.Value.TrackedValues>) {
        editingAchievement.value = editingAchievement.value.copy(
            measurementType = (editingAchievement.value.measurementType as MeasurementType.Value)
                .copy(
                    trackedValues = trackedValues
                )
        )
    }

    fun save(
        onResult: (Boolean) -> Unit
    ) {
        var achievement = editingAchievement.value
        var hasError = false

        if (achievement.name.isEmpty()) {
            hasError = true
            nameError.value = R.string.name_error_message
        }

        if (!hasError) {
            // achievement = achievement.copy(checkList = editingCheckList.value)

            viewModelScope.launch {
                onResult(
                    if (isEditing) {
                        achievementRepository.updateAchievement(achievement)
                    } else {
                        achievementRepository.insertAchievement(achievement)
                    }
                )
            }
        }

        editingAchievement.value.let { achievement ->
            if (achievement.name.isEmpty()) {
                return
            }
        }
    }

    fun delete(
        onResult: (Boolean) -> Unit
    ) {
        var achievement = editingAchievement.value

        viewModelScope.launch {
            onResult(achievementRepository.deleteAchievement(achievement))
        }
    }
}