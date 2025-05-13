package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.AchievementDB
import com.apphico.core_repository.calendar.room.entities.AchievementRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao : BaseDao<AchievementDB> {

    @Transaction
    @Query("SELECT * FROM AchievementDB WHERE achievementId = :achievementId")
    fun getAchievement(achievementId: Long): AchievementRelations

    @Transaction
    @Query(
        "SELECT * FROM AchievementDB " +
                "WHERE (:nullableGroupIdsFlag OR achievementGroupId IN (:groupIds)) " +
                "  AND (:statusAllFlag " +
                "   OR (:statusDoneFlag AND doneDate IS NOT NULL) " +
                "   OR (:statusUndoneFlag AND doneDate IS NULL)) " +
                "ORDER BY endDate"
    )
    fun getAll(
        statusAllFlag: Boolean,
        statusDoneFlag: Boolean,
        statusUndoneFlag: Boolean,
        nullableGroupIdsFlag: Boolean,
        groupIds: List<Long>
    ): Flow<List<AchievementRelations>>
}