package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = GroupDB::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("achievementGroupId")
        )
    ]
)
data class AchievementDB(
    @PrimaryKey(autoGenerate = true) val achievementId: Long = 0,
    @ColumnInfo("achievementName") val name: String,
    val description: String?,
    val achievementGroupId: Long?,
    val endDate: LocalDate?,
    val doneDate: LocalDate?,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AchievementDB::class,
            parentColumns = arrayOf("achievementId"),
            childColumns = arrayOf("achievementPercentageProgressId")
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

/*
data class Value(
    val unit: MeasurementValueUnit? = null,
    val startingValue: Float,
    val goalValue: Float,
    val trackedValues: List<TrackedValues> = emptyList()
)

data class TrackedValues(
    val trackedValue: Float,
    val description: String? = null
)
*/

data class AchievementRelations(
    @Embedded val achievementDB: AchievementDB,
    @Relation(parentColumn = "achievementGroupId", entityColumn = "groupId") val groupDB: GroupDB?,
    @Relation(parentColumn = "achievementId", entityColumn = "checkListAchievementId") val checkList: List<CheckListWithDone>?,
    @Relation(parentColumn = "achievementId", entityColumn = "achievementPercentageProgressId") val percentageProgress: List<PercentageProgressDB>?
)