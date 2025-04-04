package com.apphico.core_model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.apphico.core_model.serializers.LocalDateSerializer
import com.apphico.core_model.serializers.LocalTimeSerializer
import com.apphico.extensions.toMillis
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Parcelize
@Serializable
sealed class RecurringTask(override val title: Int) : CheckBoxItem() {
    data object ThisTask : RecurringTask(R.string.recurring_task_this)
    data object Future : RecurringTask(R.string.recurring_task_future)
    data object All : RecurringTask(R.string.recurring_task_all)
}

@Stable
@Immutable
@Parcelize
@Serializable
data class Task(
    val id: Long = 0,
    val isSaved: Boolean = true,
    val name: String = "",
    val description: String? = null,
    val group: Group? = null,
    val checkList: List<CheckListItem> = emptyList(),
    @Serializable(with = LocalDateSerializer::class) val startDate: LocalDate? = null,
    @Serializable(with = LocalTimeSerializer::class) val startTime: LocalTime? = null,
    @Serializable(with = LocalDateSerializer::class) val endDate: LocalDate? = null,
    @Serializable(with = LocalTimeSerializer::class) val endTime: LocalTime? = null,
    val daysOfWeek: List<Int> = emptyList(),
    val reminder: Reminder? = null,
    val location: Location? = null,
    val hasDone: Boolean? = null,
    val doneDates: String? = "",
    val hasDeleted: Boolean? = null,
    val deletedDates: String? = ""
) : Parcelable {
    fun key() = this.id + (this.startDate?.toMillis() ?: 0L) + (this.startTime?.toNanoOfDay() ?: 0L)
    fun isRepeatable() = (startDate != null && endDate != null && startDate > endDate) || daysOfWeek.isNotEmpty()
    fun isDone(): Boolean = doneDates?.contains(startDate.toString()) == true || (startDate == null && hasDone == true)
    fun isDeleted(): Boolean = deletedDates?.contains(startDate.toString()) == true || (startDate == null && hasDeleted == true)
    fun reminderDateTime(): LocalDateTime? {
        if (this.startDate != null && this.startTime != null && reminder != null) {
            return LocalDateTime.of(this.startDate, this.startTime)
                .minusDays(reminder.days.toLong())
                .minusHours(reminder.hours.toLong())
                .minusMinutes(reminder.minutes.toLong())
        }

        return null
    }
}