package com.apphico.core_repository.calendar.room.entities

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
            childColumns = arrayOf("achievementPercentageProgressId"),
            onDelete = CASCADE
        )
    ]
)
data class PercentageProgressDB(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val achievementPercentageProgressId: Long?,
    val progress: Float,
    val description: String?,
    val date: LocalDate?,
    val time: LocalTime?
)