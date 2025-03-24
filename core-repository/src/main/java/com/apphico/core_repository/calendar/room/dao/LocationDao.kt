package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apphico.core_repository.calendar.room.entities.LocationDB
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(location: LocationDB): Long

    @Query("DELETE FROM locationdb WHERE locationTaskId = :taskId")
    suspend fun delete(taskId: Long)
}