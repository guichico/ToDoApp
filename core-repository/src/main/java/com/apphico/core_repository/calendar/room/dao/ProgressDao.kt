package com.apphico.core_repository.calendar.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.ProgressDB
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao : BaseDao<ProgressDB> {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM progressdb")
    fun getAll(): Flow<List<ProgressDB>>

    @Transaction
    @Query("DELETE FROM progressdb WHERE achievementProgressId = :achievementId AND id NOT IN (:progressIds)")
    suspend fun deleteAll(achievementId: Long, progressIds: List<Long>)
}