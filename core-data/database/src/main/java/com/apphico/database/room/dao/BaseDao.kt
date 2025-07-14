package com.apphico.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BaseDao<T> {

    @Insert()
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(obj: T): Long

    @Transaction
    @Insert
    suspend fun insertAll(list: List<T>): List<Long>

    @Update
    suspend fun update(obj: T)

    @Transaction
    @Update
    suspend fun updateAll(list: List<T>)

    @Delete
    suspend fun delete(obj: T)
}
