package com.apphico.core_repository.calendar.settings

import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.TaskStatus
import com.apphico.core_repository.calendar.datastore.UserSettingsDataStore
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getViewMode(): Flow<CalendarViewMode>
    suspend fun setViewMode(calendarViewMode: CalendarViewMode)

    fun getTaskStatus(): Flow<TaskStatus>
    suspend fun setTaskStatus(taskStatus: TaskStatus)
}

class UserSettingsRepositoryImpl(
    private val userSettingsDataStore: UserSettingsDataStore
) : UserSettingsRepository {

    override fun getViewMode(): Flow<CalendarViewMode> = userSettingsDataStore.viewMode

    override suspend fun setViewMode(calendarViewMode: CalendarViewMode) {
        userSettingsDataStore.setCalendarViewMode(calendarViewMode)
    }

    override fun getTaskStatus(): Flow<TaskStatus> = userSettingsDataStore.taskStatus

    override suspend fun setTaskStatus(taskStatus: TaskStatus) {
        userSettingsDataStore.setTaskStatus(taskStatus)
    }
}