package com.apphico.core_repository.calendar.settings

import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.datastore.UserSettingsDataStore
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getViewMode(): Flow<CalendarViewMode>
    suspend fun setViewMode(calendarViewMode: CalendarViewMode)

    fun getTaskStatus(): Flow<Status>
    suspend fun setTaskStatus(taskStatus: Status)
}

class UserSettingsRepositoryImpl(
    private val userSettingsDataStore: UserSettingsDataStore
) : UserSettingsRepository {

    override fun getViewMode(): Flow<CalendarViewMode> = userSettingsDataStore.viewMode

    override suspend fun setViewMode(calendarViewMode: CalendarViewMode) {
        userSettingsDataStore.setCalendarViewMode(calendarViewMode)
    }

    override fun getTaskStatus(): Flow<Status> = userSettingsDataStore.taskStatus

    override suspend fun setTaskStatus(taskStatus: Status) {
        userSettingsDataStore.setTaskStatus(taskStatus)
    }
}