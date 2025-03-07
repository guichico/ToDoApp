package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.apphico.core_repository.calendar.room.LocationDB

@Dao
interface LocationDao {
    @Query("SELECT * FROM locationdb")
    fun getAll(): List<LocationDB>

    @Query("SELECT * FROM locationdb WHERE locationId IN (:locationId)")
    fun getLocation(locationId: Long): List<LocationDB>

    @Insert
    suspend fun insert(location: LocationDB) : Long

    @Delete
    suspend fun delete(location: LocationDB)
}