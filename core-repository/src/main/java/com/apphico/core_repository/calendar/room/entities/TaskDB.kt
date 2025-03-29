package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = GroupDB::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("taskGroupId")
        )
    ]
)
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
    @Embedded val reminder: ReminderDB?
)

data class ReminderDB(
    @ColumnInfo("reminder_days") val days: Int,
    @ColumnInfo("reminder_hours") val hours: Int,
    @ColumnInfo("reminder_minutes") val minutes: Int
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskDB::class,
            parentColumns = arrayOf("taskId"),
            childColumns = arrayOf("taskDoneId"),
            onDelete = CASCADE
        )
    ]
)
data class TaskDoneDB(
    @PrimaryKey(autoGenerate = true) val doneId: Long = 0,
    val taskDoneId: Long,
    val doneDate: LocalDate,
    val taskDate: LocalDate?
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskDB::class,
            parentColumns = arrayOf("taskId"),
            childColumns = arrayOf("taskDeleteId"),
            onDelete = CASCADE
        )
    ]
)
data class TaskDeletedDB(
    @PrimaryKey(autoGenerate = true) val deletedId: Long = 0,
    val taskDeleteId: Long,
    val deletedDate: LocalDate,
    val taskDate: LocalDate?
)

@DatabaseView(
    "SELECT taskDB.*, taskDoneDates.hasDone, taskDoneDates.doneDates, taskDeletedDates.hasDeleted, taskDeletedDates.deletedDates " +
            "FROM taskDB " +
            "LEFT OUTER JOIN " +
            "( " +
            "SELECT taskDoneId, 1 AS hasDone, group_concat(taskDate) AS doneDates " +
            "FROM taskDoneDb " +
            "GROUP BY taskDoneId " +
            ") AS taskDoneDates " +
            "ON taskId = taskDoneId " +
            "LEFT OUTER JOIN " +
            "( " +
            "SELECT taskDeleteId, 1 AS hasDeleted, group_concat(taskDate) AS deletedDates " +
            "FROM TaskDeletedDb " +
            "GROUP BY taskDeleteId " +
            ") AS taskDeletedDates " +
            "ON taskId = taskDeleteId "
)
data class TaskComplete(
    @Embedded val taskDB: TaskDB,
    @ColumnInfo("hasDone") val hasDone: Boolean?,
    @ColumnInfo("doneDates") val doneDates: String?,
    @ColumnInfo("hasDeleted") val hasDeleted: Boolean?,
    @ColumnInfo("deletedDates") val deletedDates: String?,
)

data class TaskWithRelations(
    @Embedded val taskComplete: TaskComplete,
    @Relation(parentColumn = "taskGroupId", entityColumn = "groupId") val groupDB: GroupDB?,
    @Relation(parentColumn = "taskId", entityColumn = "checkListTaskId") val checkList: List<CheckListWithDone>?,
    @Relation(parentColumn = "taskId", entityColumn = "locationTaskId") val locationDB: LocationDB?,
)