package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class TaskDB(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    @ColumnInfo("task_name") val name: String,
    val description: String?,
    val taskGroupId: Long?,
    val startDate: LocalDate?,
    val startTime: LocalTime?,
    val endDate: LocalDate?,
    val endTime: LocalTime?,
    val daysOfWeek: List<Int>?,
    val reminder: LocalTime?
)

@Entity
data class TaskDoneDB(
    @PrimaryKey(autoGenerate = true) val doneId: Long = 0,
    val taskDoneId: Long,
    val doneDate: LocalDate,
    val taskDate: LocalDate?
)

@Entity
data class TaskDeletedDB(
    @PrimaryKey(autoGenerate = true) val deletedId: Long = 0,
    val taskDeleteId: Long,
    val deletedDate: LocalDate,
    val taskDate: LocalDate?
)

data class TaskWithRelations(
    @Embedded val taskDB: TaskDB,
    @Relation(parentColumn = "taskGroupId", entityColumn = "groupId") val groupDB: GroupDB?,
    @Relation(parentColumn = "taskId", entityColumn = "checkListTaskId") val checkList: List<CheckListWithDone>?,
    @Relation(parentColumn = "taskId", entityColumn = "locationTaskId") val locationDB: LocationDB?,
    @ColumnInfo("hasDone") val hasDone: Boolean?,
    @ColumnInfo("doneDates") val doneDates: String?,
)