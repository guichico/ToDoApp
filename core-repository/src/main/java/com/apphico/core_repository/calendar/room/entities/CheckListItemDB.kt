package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class CheckListItemDB(
    @PrimaryKey(autoGenerate = true) val checkListItemId: Long = 0,
    val checkListTaskId: Long,
    val name: String
)

@Entity
data class CheckListItemDoneDB(
    @PrimaryKey(autoGenerate = true) val doneId: Long = 0,
    val checkListItemDoneId: Long,
    val doneDate: LocalDate,
    val taskDate: LocalDate?
)

@DatabaseView(
    "SELECT checklistitemdb.*, checkListDoneDates.checkListItemHasDone, checkListDoneDates.checkListItemDoneDates " +
            "FROM checklistitemdb " +
            "LEFT OUTER JOIN " +
            "( " +
            "    SELECT checkListItemDoneId, 1 AS checkListItemHasDone, group_concat(taskDate) AS checkListItemDoneDates " +
            "    FROM checklistitemdonedb " +
            "    GROUP BY checkListItemDoneId " +
            ") AS checkListDoneDates " +
            "ON checklistitemdb.checkListItemId = checkListDoneDates.checkListItemDoneId"
)
data class CheckListWithDone(
    @Embedded val checkListItem: CheckListItemDB,
    @ColumnInfo("checkListItemHasDone") val checkListItemHasDone: Boolean?,
    @ColumnInfo("checkListItemDoneDates") val checkListItemDoneDates: String?,
)