package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDoneDao {

    @Transaction
    @Query("SELECT * FROM taskDoneDB WHERE taskDoneId = :taskId AND (taskDate = :taskDate OR taskDate is null)")
    fun getDone(taskId: Long, taskDate: LocalDate?): Flow<TaskDoneDB?>

    @Insert
    suspend fun insert(taskDoneDB: TaskDoneDB): Long

    @Query("DELETE FROM taskDoneDB WHERE taskDoneId = :taskId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(taskId: Long, taskDate: LocalDate?)
}