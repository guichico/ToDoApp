package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apphico.core_repository.calendar.room.entities.GroupDB
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM groupdb")
    fun getAll(): Flow<List<GroupDB>>

    @Query("SELECT * FROM groupdb WHERE groupId IN (:groupId)")
    fun getGroup(groupId: Long): Flow<GroupDB>

    @Insert
    suspend fun insert(groupDB: GroupDB): Long

    @Transaction
    @Insert
    suspend fun insert(groupDB: List<GroupDB>): List<Long>

    @Update
    suspend fun update(groupDB: GroupDB)

    @Delete
    suspend fun delete(groupDB: GroupDB)
}