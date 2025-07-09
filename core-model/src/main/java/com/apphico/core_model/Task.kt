package com.apphico.core_model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.apphico.core_model.serializers.LocalDateSerializer
import com.apphico.core_model.serializers.LocalTimeSerializer
import com.apphico.extensions.getInt
import com.apphico.extensions.isAfterRightNotNull
import com.apphico.extensions.toMillis
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.stream.Stream

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
    val reminderId: Long = 0,
    val reminder: Reminder? = null,
    val location: Location? = null,
    val hasDone: Boolean? = null,
    val doneDates: String? = "",
    val hasDeleted: Boolean? = null,
    val deletedDates: String? = ""
) : Parcelable {
    fun key() = this.id + (this.reminderDateTime()?.toMillis() ?: ((this.startDate?.toMillis() ?: 0L) + (this.startTime?.toNanoOfDay() ?: 0L)))
    fun isRepeatable() = daysOfWeek.isNotEmpty() && (endDate == null || endDate.isAfterRightNotNull(startDate))
    fun isDone(): Boolean = doneDates?.contains(startDate.toString()) == true || (startDate == null && hasDone == true)
    fun isDeleted(): Boolean = deletedDates?.contains(startDate.toString()) == true || (startDate == null && hasDeleted == true)

    fun getStartDateTime(): LocalDateTime? = LocalDateTime.of(startDate, startTime).takeIf { this.startDate != null && this.startTime != null }
    fun getEndDateTime(): LocalDateTime? = LocalDateTime.of(endDate, endTime).takeIf { this.endDate != null && this.endTime != null }

    fun reminderDateTime(): LocalDateTime? {
        if (this.startDate != null && this.startTime != null && reminder != null) {
            return getStartDateTime()!!
                .minusDays(reminder.days.toLong())
                .minusHours(reminder.hours.toLong())
                .minusMinutes(reminder.minutes.toLong())
        }

        return null
    }

    fun reminderDateTime(date: LocalDate?): LocalDateTime? {
        if (date != null && this.startTime != null && reminder != null) {
            return LocalDateTime.of(date, this.startTime)
                .minusDays(reminder.days.toLong())
                .minusHours(reminder.hours.toLong())
                .minusMinutes(reminder.minutes.toLong())
        }

        return null
    }
}

fun List<Task>.sortByStartDate() =
    this.sortedWith(
        compareBy<Task> { task ->
            task.startDate != null
        }
            .thenBy { task ->
                task.startDate?.let {
                    LocalDateTime.of(it, task.startTime ?: it.atStartOfDay().toLocalTime())
                }
            }
            .thenBy(nullsLast()) { task ->
                task.endDate?.let {
                    LocalDateTime.of(it, task.endTime ?: it.atStartOfDay().toLocalTime())
                }
            }
            .thenBy { it.endTime }
            .thenBy { it.id }
    )

private fun List<Task>.filterTasks(status: Status): List<Task> =
    this.filter { task ->
        !task.isDeleted() && when (status) {
            Status.ALL -> true
            Status.DONE -> task.isDone()
            Status.UNDONE -> !task.isDone()
        }
    }

private fun Stream<Task>.filterTasks(status: Status): List<Task> = this.toList().filterTasks(status)

private fun Task.addFutureTasks(
    selectedDate: LocalDate,
    status: Status
): List<Task> {
    val startDate = this.startDate
    // TODO Check how long to view
    val endDate = (this.endDate ?: selectedDate.plusYears(1)).plusDays(1)

    return if (startDate != null && selectedDate < endDate) {
        val beginShowDate = if (selectedDate > startDate) selectedDate else startDate

        beginShowDate.datesUntil(endDate)
            .filter {
                val allDays = DayOfWeek.entries.map { daysOfWeek -> daysOfWeek.getInt() }
                val taskDaysOfWeek = this.daysOfWeek.ifEmpty { allDays }

                it.dayOfWeek.getInt() in taskDaysOfWeek
            }
            .map { newDate -> this.copy(startDate = newDate, isSaved = false) }
            .filterTasks(status)
    } else emptyList()
}

fun List<Task>.toCalendarOrder(fromStartDate: LocalDate, status: Status): List<Task> {
    return mutableListOf<Task>()
        .apply {
            // One time tasks
            addAll(this@toCalendarOrder.filter { !it.isRepeatable() }.filterTasks(status))

            // Recurring tasks
            this@toCalendarOrder.filter { it.isRepeatable() }
                .forEach { task -> addAll(task.addFutureTasks(fromStartDate, status)) }
        }
        .sortByStartDate()
}