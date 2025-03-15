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
    @Query("SELECT * FROM locationdb")
    fun getAll(): Flow<List<LocationDB>>

    @Query("SELECT * FROM locationdb WHERE locationId IN (:locationId)")
    fun getLocation(locationId: Long): Flow<LocationDB>

    @Insert
    suspend fun insert(location: LocationDB): Long

    @Update
    suspend fun update(location: LocationDB)

    @Delete
    suspend fun delete(location: LocationDB)
}