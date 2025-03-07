package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.CheckListItemDB

@Dao
interface CheckListItemDao {
    @Query("SELECT * FROM checklistitemdb")
    fun getAll(): List<CheckListItemDB>

    @Query("SELECT * FROM checklistitemdb WHERE checkListItemId IN (:checkListItemId)")
    fun getCheckListItem(checkListItemId: Long): List<CheckListItemDB>

    @Transaction
    @Insert
    suspend fun insertAll(checkListItems: List<CheckListItemDB>): List<Long>

    @Transaction
    @Delete
    suspend fun deleteAll(checkListItems: List<CheckListItemDB>)
}