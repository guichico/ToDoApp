package com.apphico.core_repository.calendar.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apphico.core_repository.calendar.room.converters.DateConverters
import com.apphico.core_repository.calendar.room.converters.ListConverters
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.AchievementDB
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import com.apphico.core_repository.calendar.room.entities.CheckListWithDone
import com.apphico.core_repository.calendar.room.entities.GroupDB
import com.apphico.core_repository.calendar.room.entities.LocationDB
import com.apphico.core_repository.calendar.room.entities.ProgressDB
import com.apphico.core_repository.calendar.room.entities.TaskComplete
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        GroupDB::class,
        TaskDB::class,
        TaskDoneDB::class,
        TaskDeletedDB::class,
        AchievementDB::class,
        ProgressDB::class,
        CheckListItemDB::class,
        CheckListItemDoneDB::class,
        LocationDB::class
    ],
    views = [TaskComplete::class, CheckListWithDone::class]
)
@TypeConverters(DateConverters::class, ListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
    abstract fun taskDoneDao(): TaskDoneDao
    abstract fun taskDeletedDao(): TaskDeletedDao
    abstract fun achievementDao(): AchievementDao
    abstract fun progressDao(): ProgressDao
    abstract fun checkListItemDao(): CheckListItemDao
    abstract fun checkListItemDoneDao(): CheckListItemDoneDao
    abstract fun locationDao(): LocationDao
}