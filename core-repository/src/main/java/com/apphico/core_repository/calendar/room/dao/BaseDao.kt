package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert
    suspend fun insert(obj: T): Long

    @Transaction
    @Insert
    suspend fun insertAll(list: List<T>): List<Long>

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)
}
