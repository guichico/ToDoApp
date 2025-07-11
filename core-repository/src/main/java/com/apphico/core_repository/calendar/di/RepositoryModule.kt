package com.apphico.core_repository.calendar.di

import android.content.Context
import com.apphico.core_repository.calendar.achievements.AchievementRepository
import com.apphico.core_repository.calendar.achievements.AchievementRepositoryImpl
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepositoryImpl
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.core_repository.calendar.checklist.CheckListRepositoryImpl
import com.apphico.core_repository.calendar.datastore.UserSettingsDataStore
import com.apphico.core_repository.calendar.focus.FocusRepository
import com.apphico.core_repository.calendar.focus.FocusRepositoryImpl
import com.apphico.core_repository.calendar.group.GroupRepository
import com.apphico.core_repository.calendar.group.GroupRepositoryImpl
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.core_repository.calendar.location.LocationRepositoryImpl
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.settings.UserSettingsRepository
import com.apphico.core_repository.calendar.settings.UserSettingsRepositoryImpl
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
class RepositoryModule() {

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