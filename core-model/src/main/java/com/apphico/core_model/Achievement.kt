package com.apphico.core_model

import android.os.Parcelable
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

enum class MeasurementValueUnit { INT, DECIMAL, CURRENCY }

@Parcelize
sealed class MeasurementType : Parcelable {

    data object None : MeasurementType()

    @Parcelize
    data class TaskDone(
        val checkList: List<CheckListItem> = emptyList()
    ) : MeasurementType()

    @Parcelize
    data class Percentage(
        val percentageProgress: List<PercentageProgress> = emptyList()
    ) : MeasurementType() {
        @Parcelize
        data class PercentageProgress(
            val progress: Float,
            val date: LocalDateTime,
            val description: String? = null
        ) : Parcelable
    }

    @Parcelize
    data class Value(
        val unit: MeasurementValueUnit? = null,
        val startingValue: Float,
        val goalValue: Float,
        val trackedValues: List<TrackedValues> = emptyList()
    ) : MeasurementType() {
        @Parcelize
        data class TrackedValues(
            val trackedValue: Float,
            val date: LocalDateTime,
            val description: String? = null
        ) : Parcelable
    }
}

@Parcelize
data class Achievement(
    val id: Long = Long.MIN_VALUE,
    val name: String = "",
    val description: String? = null,
    val group: Group? = null,
    val measurementType: MeasurementType? = null,
    val endDate: LocalDateTime? = null,
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