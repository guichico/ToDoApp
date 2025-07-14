package com.apphico.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apphico.database.room.converters.DateConverters
import com.apphico.database.room.converters.ListConverters
import com.apphico.database.room.dao.AchievementDao
import com.apphico.database.room.dao.CheckListItemDao
import com.apphico.database.room.dao.CheckListItemDoneDao
import com.apphico.database.room.dao.GroupDao
import com.apphico.database.room.dao.LocationDao
import com.apphico.database.room.dao.ProgressDao
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.dao.TaskDeletedDao
import com.apphico.database.room.dao.TaskDoneDao
import com.apphico.database.room.entities.AchievementDB
import com.apphico.database.room.entities.CheckListItemDB
import com.apphico.database.room.entities.CheckListItemDoneDB
import com.apphico.database.room.entities.CheckListWithDone
import com.apphico.database.room.entities.GroupDB
import com.apphico.database.room.entities.LocationDB
import com.apphico.database.room.entities.ProgressDB
import com.apphico.database.room.entities.TaskComplete
import com.apphico.database.room.entities.TaskDB
import com.apphico.database.room.entities.TaskDeletedDB
import com.apphico.database.room.entities.TaskDoneDB

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