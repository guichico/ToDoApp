package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apphico.core_repository.calendar.room.TaskDB
import com.apphico.core_repository.calendar.room.TaskRelations
import java.time.LocalDate

@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM taskdb WHERE endDate > :fromStartDate ORDER BY startDate")
    fun getAll(fromStartDate: LocalDate): List<TaskRelations>

    @Transaction
    @Query("SELECT * FROM taskdb WHERE :date BETWEEN date(startDate) AND date(endDate) ORDER BY time(startDate)")
    fun getFromDay(date: LocalDate): List<TaskRelations>

    @Transaction
    @Query("SELECT * FROM taskdb WHERE taskId IN (:taskId)")
    fun getTask(taskId: Long): List<TaskRelations>

    @Insert
    suspend fun insert(taskDB: TaskDB): Long

    @Transaction
    @Insert
    suspend fun insert(tasks: List<TaskDB>): List<Long>

    @Update
    suspend fun update(taskDB: TaskDB)

    @Delete
    suspend fun delete(taskDB: TaskDB)
}