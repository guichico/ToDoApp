package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AchievementDB::class,
            parentColumns = arrayOf("achievementId"),
            childColumns = arrayOf("achievementProgressId"),
            onDelete = CASCADE
        )
    ]
)
data class ProgressDB(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) val achievementProgressId: Long?,
    val progress: Float,
    val description: String?,
    val date: LocalDate?,
    val time: LocalTime?
)