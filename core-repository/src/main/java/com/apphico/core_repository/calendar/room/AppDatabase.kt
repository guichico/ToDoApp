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
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.GroupDB
import com.apphico.core_repository.calendar.room.entities.LocationDB
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB

@Database(
    version = 1,
    exportSchema = false,
    entities = [TaskDB::class, TaskDoneDB::class, GroupDB::class, CheckListItemDB::class, LocationDB::class],
)
@TypeConverters(DateConverters::class, ListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskDoneDao(): TaskDoneDao
    abstract fun groupDao(): GroupDao
    abstract fun checkListItemDao(): CheckListItemDao
    abstract fun locationDao(): LocationDao
}