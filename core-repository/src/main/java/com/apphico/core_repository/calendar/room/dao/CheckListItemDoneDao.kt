package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import java.time.LocalDate

@Dao
interface CheckListItemDoneDao : BaseDao<CheckListItemDoneDB> {
    @Transaction
    @Query("DELETE FROM checkListItemDoneDB WHERE checkListItemDoneId = :checkListItemId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(checkListItemId: Long, taskDate: LocalDate?)

    @Transaction
    @Query("DELETE FROM checkListItemDoneDB WHERE checkListItemDoneId IN (:checkListItemIds)")
    suspend fun deleteAll(checkListItemIds: List<Long>)
}