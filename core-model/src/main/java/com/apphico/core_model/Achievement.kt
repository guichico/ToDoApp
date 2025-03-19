package com.apphico.core_model

import android.os.Parcelable
import com.apphico.core_model.serializers.LocalDateTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
enum class MeasurementValueUnit { INT, DECIMAL, CURRENCY }

@Parcelize
@Serializable
sealed class MeasurementType(override val title: Int) : CheckBoxItem() {

    @Parcelize
    @Serializable
    data object None : MeasurementType(R.string.achievement_goal_type_none)

    @Parcelize
    @Serializable
    data class TaskDone(
        val checkList: List<CheckListItem> = emptyList()
    ) : MeasurementType(R.string.achievement_goal_type_step)

    @Parcelize
    @Serializable
    data class Percentage(
        val percentageProgress: List<PercentageProgress> = emptyList()
    ) : MeasurementType(R.string.achievement_goal_type_percentage) {
        @Parcelize
        @Serializable
        data class PercentageProgress(
            val progress: Float,
            @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime,
            val description: String? = null
        ) : Parcelable
    }

    @Parcelize
    @Serializable
    data class Value(
        val unit: MeasurementValueUnit? = null,
        val startingValue: Float,
        val goalValue: Float,
        val trackedValues: List<TrackedValues> = emptyList()
    ) : MeasurementType(R.string.achievement_goal_type_value) {
        @Parcelize
        @Serializable
        data class TrackedValues(
            val trackedValue: Float,
            @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime,
            val description: String? = null
        ) : Parcelable
    }
}

@Parcelize
@Serializable
data class Achievement(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val group: Group? = null,
    val measurementType: MeasurementType? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDate: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val doneDate: LocalDateTime? = null
) : Parcelable {
    fun getProgress() = when {
        doneDate != null -> 1.0f
        measurementType is MeasurementType.TaskDone -> {
            val doneTasksCount = measurementType.checkList.filter { it.isDone }.size
            doneTasksCount.toFloat() / measurementType.checkList.size.toFloat()
        }

        measurementType is MeasurementType.Percentage -> measurementType.percentageProgress.last().progress
        measurementType is MeasurementType.Value -> {
            val track = measurementType.goalValue - measurementType.startingValue

            when {
                measurementType.trackedValues.isNotEmpty() -> {
                    val progress = (measurementType.startingValue - measurementType.trackedValues.last().trackedValue) / track
                    if (progress < 0) progress * -1 else progress
                }

                else -> 0f
            }
        }

        else -> 0f
    }
}