package com.apphico.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.database.room.entities.AchievementDB
import com.apphico.database.room.entities.AchievementRelations
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
                "ORDER BY endDate"
    )
    fun getAll(
        nullableGroupIdsFlag: Boolean,
        groupIds: List<Long>
    ): Flow<List<AchievementRelations>>
}