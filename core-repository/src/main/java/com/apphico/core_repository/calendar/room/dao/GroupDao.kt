package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.apphico.core_repository.calendar.room.GroupDB

@Dao
interface GroupDao {
    @Query("SELECT * FROM groupdb")
    fun getAll(): List<GroupDB>

    @Query("SELECT * FROM groupdb WHERE groupId IN (:groupId)")
    fun getGroup(groupId: Long): List<GroupDB>

    @Insert
    suspend fun insert(groupDB: GroupDB) : Long

    @Delete
    suspend fun delete(groupDB: GroupDB)
}