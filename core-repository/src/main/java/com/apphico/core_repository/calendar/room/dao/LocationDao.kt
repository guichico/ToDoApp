package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.LocationDB

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(location: LocationDB): Long

    @Transaction
    @Query("DELETE FROM locationdb WHERE locationTaskId = :taskId")
    suspend fun delete(taskId: Long)
}