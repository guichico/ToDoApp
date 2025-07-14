package com.apphico.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.apphico.database.room.entities.GroupDB
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao : BaseDao<GroupDB> {
    @Query("SELECT * FROM groupdb")
    fun getAll(): Flow<List<GroupDB>>
}