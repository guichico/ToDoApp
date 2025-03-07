package com.apphico.core_repository.calendar.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apphico.core_repository.calendar.room.converters.DateConverters
import com.apphico.core_repository.calendar.room.converters.ListConverters
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao

@Database(entities = [TaskDB::class, GroupDB::class, CheckListItemDB::class, LocationDB::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class, ListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun groupDao(): GroupDao
    abstract fun checkListItemDao(): CheckListItemDao
    abstract fun locationDao(): LocationDao
}