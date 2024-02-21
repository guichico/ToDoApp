package com.apphico.core_repository.calendar.di

import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepositoryImpl
import com.apphico.core_repository.calendar.focus.FocusRepository
import com.apphico.core_repository.calendar.focus.FocusRepositoryImpl
import com.apphico.core_repository.calendar.group.GroupRepository
import com.apphico.core_repository.calendar.group.GroupRepositoryImpl
import com.apphico.core_repository.calendar.location.LocationRepository
import com.apphico.core_repository.calendar.location.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToDoRepositoryModule {

    @Provides
    @Singleton
    fun providesCalendarRepository(): CalendarRepository = CalendarRepositoryImpl()

    @Provides
    @Singleton
    fun providesFocusRepository(): FocusRepository = FocusRepositoryImpl()

    @Provides
    @Singleton
    fun providesLocationRepository(): LocationRepository = LocationRepositoryImpl()

    @Provides
    @Singleton
    fun providesGroupRepository(): GroupRepository = GroupRepositoryImpl()

}