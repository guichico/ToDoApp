package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.TaskDB
import com.apphico.core_repository.calendar.room.TaskRelations

@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM taskdb")
    fun getAll(): List<TaskRelations>

    @Query("SELECT * FROM taskdb WHERE taskId IN (:taskId)")
    fun getTask(taskId: Long): List<TaskRelations>

    @Insert
    suspend fun insert(taskDB: TaskDB) : Long

    @Delete
    suspend fun delete(taskDB: TaskDB)
}