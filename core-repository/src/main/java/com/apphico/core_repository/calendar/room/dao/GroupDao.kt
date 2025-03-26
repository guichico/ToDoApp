package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.apphico.core_repository.calendar.room.entities.GroupDB
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao : BaseDao<GroupDB> {
    @Query("SELECT * FROM groupdb")
    fun getAll(): Flow<List<GroupDB>>
}