package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.LocationDB

@Dao
interface LocationDao : BaseDao<LocationDB> {
    @Transaction
    @Query("DELETE FROM locationdb WHERE locationTaskId = :taskId")
    suspend fun delete(taskId: Long)
}