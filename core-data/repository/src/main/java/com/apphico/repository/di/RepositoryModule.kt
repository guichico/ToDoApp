package com.apphico.repository.di

import android.content.Context
import com.apphico.datastore.AppSettingsDataStore
import com.apphico.datastore.UserSettingsDataStore
import com.apphico.database.room.AppDatabase
import com.apphico.database.room.dao.AchievementDao
import com.apphico.database.room.dao.CheckListItemDao
import com.apphico.database.room.dao.CheckListItemDoneDao
import com.apphico.database.room.dao.GroupDao
import com.apphico.database.room.dao.LocationDao
import com.apphico.database.room.dao.ProgressDao
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.dao.TaskDeletedDao
import com.apphico.database.room.dao.TaskDoneDao
import com.apphico.repository.achievements.AchievementRepository
import com.apphico.repository.achievements.AchievementRepositoryImpl
import com.apphico.repository.alarm.AlarmHelper
import com.apphico.repository.calendar.CalendarRepository
import com.apphico.repository.calendar.CalendarRepositoryImpl
import com.apphico.repository.checklist.CheckListRepository
import com.apphico.repository.checklist.CheckListRepositoryImpl
import com.apphico.repository.focus.FocusRepository
import com.apphico.repository.focus.FocusRepositoryImpl
import com.apphico.repository.group.GroupRepository
import com.apphico.repository.group.GroupRepositoryImpl
import com.apphico.repository.location.LocationRepository
import com.apphico.repository.location.LocationRepositoryImpl
import com.apphico.repository.settings.AppSettingsRepository
import com.apphico.repository.settings.AppSettingsRepositoryImpl
import com.apphico.repository.settings.UserSettingsRepository
import com.apphico.repository.settings.UserSettingsRepositoryImpl
import com.apphico.repository.task.TaskRepository
import com.apphico.repository.task.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule() {

    @Provides
    @Singleton
    fun providesAppSettingsRepository(appSettingsDataStore: AppSettingsDataStore): AppSettingsRepository =
        AppSettingsRepositoryImpl(appSettingsDataStore)

    @Provides
    @Singleton
    fun providesUserSettingsRepository(userSettingsDataStore: UserSettingsDataStore): UserSettingsRepository =
        UserSettingsRepositoryImpl(userSettingsDataStore)

    @Provides
    @Singleton
    fun providesCalendarRepository(
        taskDao: TaskDao,
        taskDoneDao: TaskDoneDao
    ): CalendarRepository = CalendarRepositoryImpl(taskDao, taskDoneDao)

    @Provides
    @Singleton
    fun providesAchievementsRepository(
        appDatabase: AppDatabase,
        achievementDao: AchievementDao,
        checkListItemDao: CheckListItemDao,
        progressDao: ProgressDao
    ): AchievementRepository =
        AchievementRepositoryImpl(appDatabase, achievementDao, checkListItemDao, progressDao)

    @Provides
    @Singleton
    fun providesFocusRepository(): FocusRepository = FocusRepositoryImpl()

    @Provides
    @Singleton
    fun providesTaskRepository(
        appDatabase: AppDatabase,
        alarmHelper: AlarmHelper,
        taskDao: TaskDao,
        taskDeletedDao: TaskDeletedDao,
        locationDao: LocationDao,
        checkListItemDao: CheckListItemDao
    ): TaskRepository = TaskRepositoryImpl(appDatabase, alarmHelper, taskDao, taskDeletedDao, locationDao, checkListItemDao)

    @Provides
    @Singleton
    fun providesGroupRepository(groupDao: GroupDao): GroupRepository = GroupRepositoryImpl(groupDao)

    @Provides
    @Singleton
    fun providesCheckListRepository(checkListItemDoneDao: CheckListItemDoneDao): CheckListRepository =
        CheckListRepositoryImpl(checkListItemDoneDao)

    @Provides
    @Singleton
    fun providesLocationRepository(@ApplicationContext appContext: Context): LocationRepository = LocationRepositoryImpl(appContext)

}