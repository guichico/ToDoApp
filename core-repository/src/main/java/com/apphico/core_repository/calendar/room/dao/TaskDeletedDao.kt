package com.apphico.core_repository.calendar.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDeletedDao : BaseDao<TaskDeletedDB> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM taskDeletedDB")
    fun getAll(): Flow<List<TaskDeletedDB>>

    @Transaction
    @Query("DELETE FROM taskDeletedDB WHERE taskDeleteId = :taskId")
    suspend fun deleteAll(taskId: Long)

    @Transaction
    @Query("DELETE FROM taskDeletedDB WHERE taskDeleteId = :taskId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(taskId: Long, taskDate: LocalDate?)
}