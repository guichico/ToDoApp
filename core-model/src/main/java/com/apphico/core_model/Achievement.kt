package com.apphico.core_model

import android.os.Parcelable
import com.apphico.core_model.serializers.LocalDateSerializer
import com.apphico.core_model.serializers.LocalTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
enum class MeasurementValueUnit(val value: Int) {
    INT(1),
    DECIMAL(2),
    CURRENCY(3)
}

@Parcelize
@Serializable
sealed class MeasurementType(val intValue: Int, override val title: Int) : CheckBoxItem() {

    @Parcelize
    @Serializable
    data object None : MeasurementType(1, R.string.achievement_goal_type_none)

    @Parcelize
    @Serializable
    data class TaskDone(
        val checkList: List<CheckListItem> = emptyList()
    ) : MeasurementType(2, R.string.achievement_goal_type_step)

    @Parcelize
    @Serializable
    data class Percentage(
        val percentageProgress: List<Progress> = emptyList()
    ) : MeasurementType(3, R.string.achievement_goal_type_percentage)

    @Parcelize
    @Serializable
    data class Value(
        val unit: MeasurementValueUnit? = null,
        val startingValue: Float = 0f,
        val goalValue: Float = 0f,
        val trackedValues: List<Progress> = emptyList()
    ) : MeasurementType(4, R.string.achievement_goal_type_value)
}

@Parcelize
@Serializable
data class Progress(
    val id: Long = 0,
    val progress: Float = 0f,
    val description: String? = null,
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate? = null,
    @Serializable(with = LocalTimeSerializer::class) val time: LocalTime? = null
) : Parcelable

@Parcelize
@Serializable
data class Achievement(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val group: Group? = null,
    val measurementType: MeasurementType? = null,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate? = null,
    @Serializable(with = LocalDateSerializer::class)
    val doneDate: LocalDate? = null
) : Parcelable {

    fun getCheckList(): List<CheckListItem> =
        try {
            (measurementType as MeasurementType.TaskDone).checkList
        } catch (_: Exception) {
            emptyList()
        }

    fun getPercentageProgress(): List<Progress> =
        try {
            (measurementType as MeasurementType.Percentage).percentageProgress
        } catch (_: Exception) {
            emptyList()
        }

    fun getValueProgress(): MeasurementType.Value? =
        try {
            (measurementType as MeasurementType.Value)
        } catch (_: Exception) {
            null
        }

    fun getProgress() = when {
        doneDate != null -> 1.0f
        measurementType is MeasurementType.TaskDone -> {
            val doneTasksCount = measurementType.checkList.filter { it.isDone(this.doneDate) }.size
            val progress = doneTasksCount.toFloat() / measurementType.checkList.size.toFloat()
            progress.takeIf { !it.isNaN() } ?: 0f
        }

        measurementType is MeasurementType.Percentage -> measurementType.percentageProgress.lastOrNull()?.progress ?: 0f
        measurementType is MeasurementType.Value -> {
            val track = measurementType.goalValue - measurementType.startingValue

            when {
                measurementType.trackedValues.isNotEmpty() -> {
                    val progress = (measurementType.startingValue - measurementType.trackedValues.last().progress) / track
                    if (progress < 0) progress * -1 else progress
                }

                else -> 0f
            }
        }

        else -> 0f
    }
}