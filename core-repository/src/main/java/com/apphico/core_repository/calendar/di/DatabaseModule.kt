package com.apphico.core_repository.calendar.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.AppDatabaseInitializer
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.dao.ReminderIdDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule() {

    @Provides
    @Singleton
    fun provideGroupDao(appDatabase: AppDatabase): GroupDao = appDatabase.groupDao()

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao = appDatabase.taskDao()

    @Provides
    @Singleton
    fun provideAchievementDao(appDatabase: AppDatabase): AchievementDao = appDatabase.achievementDao()

    @Provides
    @Singleton
    fun provideProgressDao(appDatabase: AppDatabase): ProgressDao = appDatabase.progressDao()

    @Provides
    @Singleton
    fun provideTaskDoneDao(appDatabase: AppDatabase): TaskDoneDao = appDatabase.taskDoneDao()

    @Provides
    @Singleton
    fun provideTaskDeletedDao(appDatabase: AppDatabase): TaskDeletedDao = appDatabase.taskDeletedDao()

    @Provides
    @Singleton
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao = appDatabase.locationDao()

    @Provides
    @Singleton
    fun provideCheckListItemDao(appDatabase: AppDatabase): CheckListItemDao = appDatabase.checkListItemDao()

    @Provides
    @Singleton
    fun provideCheckListItemDoneDao(appDatabase: AppDatabase): CheckListItemDoneDao = appDatabase.checkListItemDoneDao()

    @Provides
    @Singleton
    fun provideReminderIdDao(appDatabase: AppDatabase): ReminderIdDao = appDatabase.reminderIdDao()

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext appContext: Context,
        groupDaoProvider: Provider<GroupDao>,
        taskDaoProvider: Provider<TaskDao>,
        checkListDaoProvider: Provider<CheckListItemDao>,
        achievementDaoProvider: Provider<AchievementDao>,
        progressDaoProvider: Provider<ProgressDao>
    ): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, "ToDoAppDB"
    )
        .addCallback(
            AppDatabaseInitializer(
                appContext = appContext,
                groupDaoProvider = groupDaoProvider,
                taskDaoProvider = taskDaoProvider,
                checkListDaoProvider = checkListDaoProvider,
                achievementDaoProvider = achievementDaoProvider,
                progressDaoProvider = progressDaoProvider
            )
        )
        .setQueryCallback(
            queryCallback = RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
                Log.d("ToDoAppDatabase", "SQL Query: $sqlQuery SQL Args: $bindArgs")
            },
            executor = Executors.newSingleThreadExecutor()
        )
        .build()
}