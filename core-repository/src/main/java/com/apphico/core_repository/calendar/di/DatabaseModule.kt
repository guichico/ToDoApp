package com.apphico.core_repository.calendar.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.AppDatabaseInitializer
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
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
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao = appDatabase.locationDao()

    @Provides
    @Singleton
    fun provideCheckListItemDao(appDatabase: AppDatabase): CheckListItemDao = appDatabase.checkListItemDao()

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext appContext: Context,
        groupProvider: Provider<GroupDao>,
        taskProvider: Provider<TaskDao>
    ): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, "ToDoAppDB"
    )
        .addCallback(
            AppDatabaseInitializer(
                groupProvider = groupProvider,
                taskProvider = taskProvider
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