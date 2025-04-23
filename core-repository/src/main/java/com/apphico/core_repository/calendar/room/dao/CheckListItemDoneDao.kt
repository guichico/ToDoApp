package com.apphico.core_repository.calendar.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CheckListItemDoneDao : BaseDao<CheckListItemDoneDB> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM checkListItemDoneDB")
    fun getAll(): Flow<List<CheckListItemDoneDB>>

    @Transaction
    @Query("DELETE FROM checkListItemDoneDB WHERE checkListItemDoneId = :checkListItemId AND (parentDate = :taskDate OR parentDate is null)")
    suspend fun delete(checkListItemId: Long, taskDate: LocalDate?)

    @Transaction
    @Query("DELETE FROM checkListItemDoneDB WHERE checkListItemDoneId IN (:checkListItemIds)")
    suspend fun deleteAll(checkListItemIds: List<Long>)
}