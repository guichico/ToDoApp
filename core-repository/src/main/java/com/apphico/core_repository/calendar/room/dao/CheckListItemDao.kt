package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckListItemDao : BaseDao<CheckListItemDB> {
    @Query("SELECT * FROM checklistitemdb")
    fun getAll(): Flow<List<CheckListItemDB>>

    @Query("SELECT * FROM checklistitemdb WHERE checkListItemId IN (:checkListItemId)")
    fun getCheckListItem(checkListItemId: Long): Flow<CheckListItemDB>

    @Transaction
    @Query("DELETE FROM checklistitemdb WHERE (checkListTaskId = :parentId OR checkListAchievementId = :parentId) AND checkListItemId NOT IN (:checkListItemIds)")
    suspend fun deleteAll(parentId: Long, checkListItemIds: List<Long>)
}