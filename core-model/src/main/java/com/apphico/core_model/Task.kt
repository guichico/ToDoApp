package com.apphico.core_model

import android.os.Parcelable
import com.apphico.core_model.serializers.LocalDateTimeSerializer
import com.apphico.core_model.serializers.LocalTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

@Parcelize
@Serializable
data class Task(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val group: Group? = null,
    val checkList: List<CheckListItem> = emptyList(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDate: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDate: LocalDateTime? = null,
    val daysOfWeek: List<Int> = emptyList(),
    @Serializable(with = LocalTimeSerializer::class)
    val reminder: LocalTime? = null,
    val location: Location? = null,
    val isDone: Boolean = false
) : Parcelable