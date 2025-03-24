package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import java.time.LocalDate

@Dao
interface TaskDeletedDao {
    @Insert
    suspend fun insert(taskDeletedDB: TaskDeletedDB): Long

    @Transaction
    @Query("DELETE FROM taskDeletedDB WHERE taskDeleteId = :taskId")
    suspend fun deleteAll(taskId: Long)

    @Transaction
    @Query("DELETE FROM taskDeletedDB WHERE taskDeleteId = :taskId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(taskId: Long, taskDate: LocalDate?)
}