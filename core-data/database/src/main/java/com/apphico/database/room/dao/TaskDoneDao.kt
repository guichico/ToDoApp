package com.apphico.database.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.database.room.entities.TaskDoneDB
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDoneDao : BaseDao<TaskDoneDB> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM taskDoneDB")
    fun getAll(): Flow<List<TaskDoneDB>>

    @Transaction
    @Query("DELETE FROM taskDoneDB WHERE taskDoneId = :taskId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(taskId: Long, taskDate: LocalDate?)
}