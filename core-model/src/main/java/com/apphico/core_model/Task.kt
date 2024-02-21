package com.apphico.core_model

import android.os.Parcelable
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val group: Group? = null,
    val checkList: List<CheckListItem> = emptyList(),
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val daysOfWeek: List<Int> = emptyList(),
    val reminder: LocalTime? = null,
    val location: Location? = null,
    val isDone: Boolean = false
) : Parcelable