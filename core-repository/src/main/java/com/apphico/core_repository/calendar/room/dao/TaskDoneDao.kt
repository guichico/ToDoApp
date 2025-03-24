package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import java.time.LocalDate

@Dao
interface TaskDoneDao {
    @Insert
    suspend fun insert(taskDoneDB: TaskDoneDB): Long

    @Query("DELETE FROM taskDoneDB WHERE taskDoneId = :taskId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(taskId: Long, taskDate: LocalDate?)
}