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
sealed class RecurringTaskSaveMethod(override val title: Int) : CheckBoxItem() {
    data object ThisTask : RecurringTaskSaveMethod(R.string.recurring_task_this)
    data object Future : RecurringTaskSaveMethod(R.string.recurring_task_future)
    data object All : RecurringTaskSaveMethod(R.string.recurring_task_all)
}

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
    @Serializable(with = LocalTimeSerializer::class) val reminder: LocalTime? = null,
    val location: Location? = null
) : Parcelable