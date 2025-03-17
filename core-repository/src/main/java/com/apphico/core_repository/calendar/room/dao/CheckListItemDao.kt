package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckListItemDao {
    @Query("SELECT * FROM checklistitemdb")
    fun getAll(): Flow<List<CheckListItemDB>>

    @Query("SELECT * FROM checklistitemdb WHERE checkListItemId IN (:checkListItemId)")
    fun getCheckListItem(checkListItemId: Long): Flow<CheckListItemDB>

    @Transaction
    @Insert
    suspend fun insertAll(checkListItems: List<CheckListItemDB>): List<Long>

    @Transaction
    @Query("DELETE FROM checklistitemdb WHERE checkListTaskId = :taskId")
    suspend fun deleteAll(taskId: Long)
}