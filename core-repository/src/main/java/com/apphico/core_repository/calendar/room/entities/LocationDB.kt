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
            childColumns = arrayOf("locationTaskId"),
            onDelete = CASCADE
        )
    ]
)
data class LocationDB(
    @PrimaryKey(autoGenerate = true) val locationId: Long = 0,
    @ColumnInfo(index = true) val locationTaskId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String?
)