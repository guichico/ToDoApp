package com.apphico.todoapp.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.core_model.Progress
import com.apphico.core_repository.calendar.achievements.AchievementRepository
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.designsystem.R
import com.apphico.extensions.add
import com.apphico.extensions.combine
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
import java.time.LocalDateTime
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
    val editingCheckList = MutableStateFlow(achievement?.getCheckList() ?: emptyList())
    val editingPercentageProgress = MutableStateFlow(achievement?.getPercentageProgress() ?: emptyList())
    val editingValueProgress = MutableStateFlow(achievement?.getValueProgress() ?: MeasurementType.Value())

    val isEditing = achievement != null

    val nameError = MutableStateFlow<Int?>(null)
    val dateError = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<Group?>(GROUP_ARG, null)
                .filterNotNull()
                .map { editingAchievement.value.copy(group = it) }
                .flowOn(Dispatchers.IO)
                .collectLatest(editingAchievement::emit)
        }

        viewModelScope.launch {
            combine(
                savedStateHandle.getStateFlow<Int?>(MEASUREMENT_TYPE_ARG, null).filterNotNull(),
                savedStateHandle.getStateFlow<Operation?>(OPERATION_ARG, null).filterNotNull()
            )
                .flowOn(Dispatchers.IO)
                .collectLatest { (measurementType, operation) ->
                    var progressList = when (measurementType) {
                        MeasurementType.Percentage().intValue -> editingPercentageProgress.value
                        MeasurementType.Value().intValue -> editingValueProgress.value.trackedValues
                        else -> emptyList()
                    }

                    progressList = when (operation) {
                        is Operation.Save -> progressList.add(operation.progress)
                        is Operation.Update -> progressList.update(operation.oldProgress, operation.progress)
                        is Operation.Delete -> progressList.remove(operation.progress)
                    }

                    progressList = progressList.sortByDate()

                    when (measurementType) {
                        MeasurementType.Percentage().intValue -> editingPercentageProgress.value = progressList
                        MeasurementType.Value().intValue -> {
                            editingValueProgress.value = editingValueProgress.value.copy(
                                trackedValues = progressList
                            )
                        }
                    }
                }
        }
    }

    fun hasChanges(): Boolean {
        val achievement = achievement ?: Achievement()
        val editingAchievement = editingAchievement.value

        return when {
            editingAchievement.name != achievement.name -> true
            editingAchievement.description != achievement.description -> true
            editingAchievement.group != achievement.group -> true
            editingAchievement.endDate != achievement.endDate -> true
            !editingAchievement.getCheckList().isEqualToBy(editingCheckList.value) { it.name } -> true
            achievement.getPercentageProgress() != editingPercentageProgress.value -> true
            achievement.getValueProgress() != editingValueProgress.value -> true
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
        dateError.value = null
    }

    fun onMeasurementTypeChanged(measurementType: MeasurementType) {
        editingAchievement.value = editingAchievement.value.copy(measurementType = measurementType)
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

    fun setCheckListItemDone(checkListItem: CheckListItem, parentDate: LocalDate?, isDone: Boolean) = viewModelScope.launch {
        if (checkListRepository.changeCheckListItemDone(checkListItem, parentDate, isDone)) {
            val newDoneDates = if (isDone) {
                checkListItem.doneDates + "${if (checkListItem.doneDates.isNullOrEmpty()) "" else ", "}, $parentDate"
            } else {
                checkListItem.doneDates?.replace(parentDate.toString(), "")
            }
            val newItem = checkListItem.copy(hasDone = isDone, doneDates = newDoneDates)

            editingCheckList.value = editingCheckList.value.update(checkListItem, newItem)
        }
    }

    fun onUnitChanged(unit: MeasurementValueUnit) {
        editingValueProgress.value = editingValueProgress.value.copy(unit = unit, startingValue = null, goalValue = null)
    }

    fun ondStartingValueChanged(startingValue: Float) {
        editingValueProgress.value = editingValueProgress.value.copy(startingValue = startingValue)
    }

    fun ondGoalValueChanged(goalValue: Float) {
        editingValueProgress.value = editingValueProgress.value.copy(goalValue = goalValue)
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

        if (achievement.endDate == null) {
            hasError = true
            dateError.value = R.string.date_error_message
        }

        if (!hasError) {
            when (achievement.measurementType) {
                is MeasurementType.TaskDone -> {
                    achievement = achievement.copy(
                        measurementType = (achievement.measurementType as MeasurementType.TaskDone).copy(checkList = editingCheckList.value)
                    )
                }

                is MeasurementType.Percentage -> {
                    achievement = achievement.copy(
                        measurementType = (achievement.measurementType as MeasurementType.Percentage).copy(percentageProgress = editingPercentageProgress.value)
                    )
                }

                is MeasurementType.Value -> {
                    achievement = achievement.copy(measurementType = editingValueProgress.value)
                }

                else -> {

                }
            }

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

    private fun List<Progress>.sortByDate() =
        this.sortedWith(
            compareBy<Progress> { progress ->
                progress.date?.let {
                    LocalDateTime.of(it, progress.time ?: it.atStartOfDay().toLocalTime())
                }
            }
                .thenBy { it.time }
                .thenBy { it.id }
        )
}