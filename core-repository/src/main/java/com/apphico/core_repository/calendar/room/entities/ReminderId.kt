package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskDB::class,
            parentColumns = arrayOf("taskId"),
            childColumns = arrayOf("reminderTaskId"),
            onDelete = CASCADE
        )
    ]
)
data class ReminderIdDB(
    @PrimaryKey(autoGenerate = false) val reminderId: Long,
    @ColumnInfo(index = true) val reminderTaskId: Long
)