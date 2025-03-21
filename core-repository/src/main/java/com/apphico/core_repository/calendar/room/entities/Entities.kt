package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class GroupDB(
    @PrimaryKey(autoGenerate = true) val groupId: Long = 0,
    @ColumnInfo("group_name") val name: String,
    val color: Int
)

@Entity
data class CheckListItemDB(
    @PrimaryKey(autoGenerate = true) val checkListItemId: Long = 0,
    val checkListTaskId: Long,
    val name: String,
    val isDone: Boolean = false,
)

@Entity
data class LocationDB(
    @PrimaryKey(autoGenerate = true) val locationId: Long = 0,
    val locationTaskId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String?
)

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

data class TaskWithRelations(
    @Embedded val taskDB: TaskDB,
    @Relation(parentColumn = "taskGroupId", entityColumn = "groupId") val groupDB: GroupDB?,
    @Relation(parentColumn = "taskId", entityColumn = "checkListTaskId") val checkList: List<CheckListItemDB>?,
    @Relation(parentColumn = "taskId", entityColumn = "locationTaskId") val locationDB: LocationDB?,
    @ColumnInfo("hasDone") val hasDone: Boolean?,
    @ColumnInfo("doneDates") val doneDates: String?,
)