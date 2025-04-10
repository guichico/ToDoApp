package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.PercentageProgressDB

@Dao
interface PercentageProgressDao : BaseDao<PercentageProgressDB> {
    @Transaction
    @Query("DELETE FROM percentageprogressdb WHERE achievementPercentageProgressId = :achievementId AND id NOT IN (:percentageProgressIds)")
    suspend fun deleteAll(achievementId: Long, percentageProgressIds: List<Long>)
}