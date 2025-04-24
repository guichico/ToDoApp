package com.apphico.core_model

import android.os.Parcelable
import com.apphico.core_model.serializers.LocalDateSerializer
import com.apphico.core_model.serializers.LocalTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
@Serializable
data class Progress(
    val id: Long = 0,
    val progress: Float? = null,
    val description: String? = null,
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate? = null,
    @Serializable(with = LocalTimeSerializer::class) val time: LocalTime? = null
) : Parcelable

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
        val progress: List<Progress> = emptyList()
    ) : MeasurementType(3, R.string.achievement_goal_type_percentage) {
        fun getProgress() = progress.lastOrNull()?.progress ?: 0f
    }

    @Parcelize
    @Serializable
    data class Value(
        val unit: MeasurementValueUnit? = null,
        val startingValue: Float? = null,
        val goalValue: Float? = null,
        val trackedValues: List<Progress> = emptyList()
    ) : MeasurementType(4, R.string.achievement_goal_type_value) {
        fun getProgress(): Float {
            val track = goalValue?.minus(startingValue ?: 0f) ?: 0f

            return when {
                trackedValues.isNotEmpty() -> {
                    val progress = (startingValue?.minus(trackedValues.last().progress ?: 0f) ?: 0f) / track
                    if (progress < 0) progress * -1 else progress
                }

                else -> 0f
            }
        }
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
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate? = null,
    @Serializable(with = LocalDateSerializer::class)
    private val doneDate: LocalDate? = null
) : Parcelable {

    fun getCheckList(): List<CheckListItem> =
        try {
            (measurementType as MeasurementType.TaskDone).checkList
        } catch (_: Exception) {
            emptyList()
        }

    fun getPercentageProgress(): MeasurementType.Percentage =
        try {
            (measurementType as MeasurementType.Percentage)
        } catch (_: Exception) {
            MeasurementType.Percentage()
        }

    fun getValueProgress(): MeasurementType.Value =
        try {
            (measurementType as MeasurementType.Value)
        } catch (_: Exception) {
            MeasurementType.Value()
        }

    fun getProgress() = when (measurementType) {
        is MeasurementType.None -> {
            if (doneDate != null) 1.0f else 0f
        }

        is MeasurementType.TaskDone -> {
            val doneTasksCount = measurementType.checkList.filter { it.isDone(this.doneDate) }.size
            val progress = doneTasksCount.toFloat() / measurementType.checkList.size.toFloat()
            progress.takeIf { !it.isNaN() } ?: 0f
        }

        is MeasurementType.Percentage -> measurementType.getProgress()
        is MeasurementType.Value -> measurementType.getProgress()
        else -> 0f
    }

    fun isDone() = this.getProgress() >= 1f

    fun getDoneDate() = if (isDone() && measurementType is MeasurementType.TaskDone) {
        getCheckList().map { it.doneDate }.sortedBy { it }.last()
    } else {
        doneDate
    }
}