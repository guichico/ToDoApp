package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import androidx.room.Update
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.TaskWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Transaction
    @RawQuery
    fun getTasks(query: RoomRawQuery): Flow<List<TaskWithRelations>>

    @Insert
    suspend fun insert(taskDB: TaskDB): Long

    @Update
    suspend fun update(taskDB: TaskDB)

    @Delete
    suspend fun delete(taskDB: TaskDB)
}