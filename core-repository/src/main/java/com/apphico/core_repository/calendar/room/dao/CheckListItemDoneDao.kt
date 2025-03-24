package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import java.time.LocalDate

@Dao
interface CheckListItemDoneDao {
    @Insert
    suspend fun insert(checkListItemDoneDB: CheckListItemDoneDB): Long

    @Query("DELETE FROM checkListItemDoneDB WHERE checkListItemDoneId = :checkListItemId AND (taskDate = :taskDate OR taskDate is null)")
    suspend fun delete(checkListItemId: Long, taskDate: LocalDate?)
}