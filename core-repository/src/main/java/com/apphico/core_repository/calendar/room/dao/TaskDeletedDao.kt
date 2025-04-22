package com.apphico.core_repository.calendar.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Query
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDeletedDao : BaseDao<TaskDeletedDB> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM taskDeletedDB")
    fun getAll(): Flow<List<TaskDeletedDB>>
}