package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

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

data class AchievementRelations(
    @Embedded val achievementDB: AchievementDB,
    @Relation(parentColumn = "achievementGroupId", entityColumn = "groupId") val groupDB: GroupDB?,
    @Relation(parentColumn = "achievementId", entityColumn = "checkListAchievementId") val checkList: List<CheckListWithDone>?
)