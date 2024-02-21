package com.apphico.todoapp.achievements

import androidx.lifecycle.ViewModel
import com.apphico.core_model.Achievement
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.todoapp.di.AddEditAchievementScreenArgModule
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class AddEditAchievementViewModel @Inject constructor(
    @AddEditAchievementScreenArgModule.AchievementData private val achievementArg: Achievement?
) : ViewModel() {

    val editingAchievement = MutableStateFlow(achievementArg ?: Achievement())
    val editingMeasurementType = MutableStateFlow<MeasurementType?>(null)
    val progress = MutableStateFlow(achievementArg?.getProgress() ?: 0f)

    val isEditing = achievementArg != null

    fun hasChanges(): Boolean {
        val achievement = achievementArg ?: Achievement()
        val editingAchievement = editingAchievement.value

        // TODO Implement others
        return when {
            editingAchievement.name != achievement.name -> true
            editingAchievement.description != achievement.description -> true
            editingAchievement.endDate != achievement.endDate -> true
            editingAchievement.doneDate != achievement.doneDate -> true
            else -> false
        }
    }

    fun onNameChanged(text: String) {
        editingAchievement.value = editingAchievement.value.copy(name = text)
    }

    fun onDescriptionChanged(text: String) {
        editingAchievement.value = editingAchievement.value.copy(description = text)
    }

    fun onGroupRemoved() {
        editingAchievement.value = editingAchievement.value.copy(group = null)
    }

    fun onEndDateChanged(date: LocalDateTime?) {
        editingAchievement.value = editingAchievement.value.copy(endDate = date)
    }

    fun onMeasurementTypeChanged(measurementType: MeasurementType) {
        editingAchievement.value = editingAchievement.value.copy(measurementType = measurementType)
        editingMeasurementType.value = measurementType
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

    fun save() {
        editingAchievement.value.let { achievement ->
            if (achievement.name.isEmpty()) {
                return
            }
        }
    }
}