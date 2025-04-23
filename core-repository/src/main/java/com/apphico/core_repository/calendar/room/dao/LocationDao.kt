package com.apphico.core_repository.calendar.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.LocationDB
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao : BaseDao<LocationDB> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM locationdb")
    fun getAll(): Flow<List<LocationDB>>

    @Transaction
    @Query("DELETE FROM locationdb WHERE locationTaskId = :taskId")
    suspend fun delete(taskId: Long)
}