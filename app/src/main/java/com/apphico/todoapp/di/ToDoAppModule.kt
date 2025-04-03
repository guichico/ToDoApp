package com.apphico.todoapp.di

import android.content.Context
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.todoapp.alarm.AlarmHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToDoAppModule {
    @Provides
    @Singleton
    fun providesAlarmHelper(@ApplicationContext context: Context): AlarmHelper = AlarmHelperImpl(context)
}
