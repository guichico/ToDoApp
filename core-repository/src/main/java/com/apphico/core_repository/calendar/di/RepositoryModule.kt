package com.apphico.core_repository.calendar.di

import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepositoryImpl
import com.apphico.core_repository.calendar.checklist.CheckListRepository
import com.apphico.core_repository.calendar.checklist.CheckListRepositoryImpl
import com.apphico.core_repository.calendar.focus.FocusRepository
import com.apphico.core_repository.calendar.focus.FocusRepositoryImpl
import com.apphico.core_repository.calendar.group.GroupRepository
import com.apphico.core_repository.calendar.group.GroupRepositoryImpl
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.core_repository.calendar.location.LocationRepositoryImpl
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.core_repository.calendar.task.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule() {

    @Provides
    @Singleton
    fun providesCalendarRepository(
        taskDao: TaskDao,
        taskDoneDao: TaskDoneDao
    ): CalendarRepository = CalendarRepositoryImpl(taskDao, taskDoneDao)

    @Provides
    @Singleton
    fun providesFocusRepository(): FocusRepository = FocusRepositoryImpl()

    @Provides
    @Singleton
    fun providesTaskRepository(
        taskDao: TaskDao,
        taskDeletedDao: TaskDeletedDao,
        locationDao: LocationDao,
        checkListItemDao: CheckListItemDao,
        checkListItemDoneDao: CheckListItemDoneDao
    ): TaskRepository = TaskRepositoryImpl(taskDao, taskDeletedDao, locationDao, checkListItemDao, checkListItemDoneDao)

    @Provides
    @Singleton
    fun providesGroupRepository(groupDao: GroupDao): GroupRepository = GroupRepositoryImpl(groupDao)

    @Provides
    @Singleton
    fun providesCheckListRepository(
        checkListItemDoneDao: CheckListItemDoneDao
    ): CheckListRepository = CheckListRepositoryImpl(checkListItemDoneDao)

    @Provides
    @Singleton
    fun providesLocationRepository(): LocationRepository = LocationRepositoryImpl()

}