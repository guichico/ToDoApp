package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.TaskWithRelations
import com.apphico.extensions.getInt
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM taskdb WHERE (:fromStartDate BETWEEN date(startDate) AND date(endDate)) OR startDate is null OR endDate is null ORDER BY startDate, startTime")
    fun getAll(fromStartDate: LocalDate): Flow<List<TaskWithRelations>>

    @Transaction
    @Query(
        "SELECT * FROM taskdb " +
                "WHERE (" +
                " ((:date BETWEEN date(startDate) AND date(endDate)) AND (daysOfWeek LIKE :dayOfWeek)) OR " +
                " ((:date BETWEEN date(startDate) AND date(endDate)) AND (daysOfWeek LIKE '[]'))" +
                ") " +
                "OR (" +
                " ((date(:date) >= date(startDate) AND endDate is null) AND (daysOfWeek LIKE :dayOfWeek)) OR" +
                " ((date(:date) == date(startDate) AND endDate is null) AND (daysOfWeek LIKE '[]')) " +
                ") " +
                "OR (startDate is null AND endDate is null AND daysOfWeek LIKE '[]') " +
                "ORDER BY startTime"
    )
    fun getFromDay(
        date: LocalDate,
        dayOfWeek: String = "%${date.dayOfWeek.getInt()}%"
    ): Flow<List<TaskWithRelations>>

    @Transaction
    @Query("SELECT * FROM taskdb WHERE taskId IN (:taskId)")
    fun getTask(taskId: Long): Flow<TaskWithRelations>

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