package com.apphico.core_repository.calendar.room.entities

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
            entity = AchievementDB::class,
            parentColumns = arrayOf("achievementId"),
            childColumns = arrayOf("achievementValueProgressId"),
            onDelete = CASCADE
        )
    ]
)
data class ValueProgressDB(
    @PrimaryKey(autoGenerate = true) val valueProgressId: Long = 0,
    val achievementValueProgressId: Long?,
    val unit: Int,
    val startingValue: Float,
    val goalValue: Float
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ValueProgressDB::class,
            parentColumns = arrayOf("valueProgressId"),
            childColumns = arrayOf("trackedValueProgressId"),
            onDelete = CASCADE
        )
    ]
)
data class TrackedValuesDB(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackedValueProgressId: Long?,
    val trackedValue: Float,
    val description: String?,
    val date: LocalDate?,
    val time: LocalTime?
)

@DatabaseView("SELECT * FROM valueprogressdb, trackedvaluesdb ON valueProgressId = trackedValueProgressId")
data class ValueProgressTrackedValues(
    @Embedded val valueProgressDB: ValueProgressDB,
    @Relation(parentColumn = "valueProgressId", entityColumn = "trackedValueProgressId") val trackedValues: List<TrackedValuesDB>?
)