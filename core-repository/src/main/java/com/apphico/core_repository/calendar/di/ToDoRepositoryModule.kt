package com.apphico.core_repository.calendar.di

import android.content.Context
import androidx.room.Room
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepositoryImpl
import com.apphico.core_repository.calendar.focus.FocusRepository
import com.apphico.core_repository.calendar.focus.FocusRepositoryImpl
import com.apphico.core_repository.calendar.group.GroupRepository
import com.apphico.core_repository.calendar.group.GroupRepositoryImpl
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.core_repository.calendar.location.LocationRepositoryImpl
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.core_repository.calendar.task.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ToDoRepositoryModule() {

    @Provides
    @Singleton
    fun providesCalendarRepository(appDatabase: AppDatabase): CalendarRepository = CalendarRepositoryImpl(appDatabase)

    @Provides
    @Singleton
    fun providesTaskRepository(appDatabase: AppDatabase): TaskRepository = TaskRepositoryImpl(appDatabase)

    @Provides
    @Singleton
    fun providesFocusRepository(): FocusRepository = FocusRepositoryImpl()

    @Provides
    @Singleton
    fun providesLocationRepository(): LocationRepository = LocationRepositoryImpl()

    @Provides
    @Singleton
    fun providesGroupRepository(appDatabase: AppDatabase): GroupRepository = GroupRepositoryImpl(appDatabase)

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "ToDoAppDB"
        )
            .createFromAsset("database/ToDoAppDB.db")
            .build()
}