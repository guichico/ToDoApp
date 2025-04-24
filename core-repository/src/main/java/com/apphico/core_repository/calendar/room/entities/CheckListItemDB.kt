package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskDB::class,
            parentColumns = arrayOf("taskId"),
            childColumns = arrayOf("checkListTaskId"),
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = AchievementDB::class,
            parentColumns = arrayOf("achievementId"),
            childColumns = arrayOf("checkListAchievementId"),
            onDelete = CASCADE
        )
    ]
)
data class CheckListItemDB(
    @PrimaryKey(autoGenerate = true) val checkListItemId: Long = 0,
    @ColumnInfo(index = true) val checkListTaskId: Long? = null,
    @ColumnInfo(index = true) val checkListAchievementId: Long? = null,
    val name: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CheckListItemDB::class,
            parentColumns = arrayOf("checkListItemId"),
            childColumns = arrayOf("checkListItemDoneId"),
            onDelete = CASCADE
        )
    ]
)
data class CheckListItemDoneDB(
    @PrimaryKey(autoGenerate = true) val doneId: Long = 0,
    @ColumnInfo(index = true) val checkListItemDoneId: Long,
    val doneDate: LocalDate,
    val parentDate: LocalDate?
)

@DatabaseView(
    "SELECT checklistitemdb.*, checkListDoneDates.checkListItemHasDone, checkListDoneDates.doneDate AS checkListItemDoneDate, checkListDoneDates.checkListItemDoneDates " +
            "FROM checklistitemdb " +
            "LEFT OUTER JOIN " +
            "( " +
            "    SELECT checkListItemDoneId, 1 AS checkListItemHasDone, doneDate, group_concat(parentDate) AS checkListItemDoneDates " +
            "    FROM checklistitemdonedb " +
            "    GROUP BY checkListItemDoneId " +
            ") AS checkListDoneDates " +
            "ON checklistitemdb.checkListItemId = checkListDoneDates.checkListItemDoneId"
)
data class CheckListWithDone(
    @Embedded val checkListItem: CheckListItemDB,
    @ColumnInfo("checkListItemHasDone") val checkListItemHasDone: Boolean?,
    @ColumnInfo("checkListItemDoneDate") val checkListItemDoneDate: LocalDate?,
    @ColumnInfo("checkListItemDoneDates") val checkListItemDoneDates: String?,
)