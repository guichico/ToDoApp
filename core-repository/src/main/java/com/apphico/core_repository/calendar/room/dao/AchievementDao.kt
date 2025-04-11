package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.AchievementDB
import com.apphico.core_repository.calendar.room.entities.AchievementRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao : BaseDao<AchievementDB> {

    @Transaction
    @Query("SELECT * FROM AchievementDB")
    fun getAll(): Flow<List<AchievementRelations>>
}