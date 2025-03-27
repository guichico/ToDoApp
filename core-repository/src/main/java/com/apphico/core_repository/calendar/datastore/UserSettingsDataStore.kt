package com.apphico.core_repository.calendar.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsDataStore(
    private val context: Context,
    dataStoreFileName: String
) : BaseDataStore(dataStoreFileName) {

    private val viewModePreference = intPreferencesKey("view_mode")
    val viewMode: Flow<CalendarViewMode> = context.dataStore.data
        .map { preferences ->
            CalendarViewMode.entries.firstOrNull { it.intValue == preferences[viewModePreference] } ?: CalendarViewMode.DAY
        }

    suspend fun setCalendarViewMode(calendarViewMode: CalendarViewMode) {
        context.dataStore.edit { settings ->
            settings[viewModePreference] = calendarViewMode.intValue
        }
    }

    private val taskStatusPreference = intPreferencesKey("task_status")
    val taskStatus: Flow<Status> = context.dataStore.data
        .map { preferences ->
            Status.entries.firstOrNull { it.intValue == preferences[taskStatusPreference] } ?: Status.ALL
        }

    suspend fun setTaskStatus(taskStatus: Status) {
        context.dataStore.edit { settings ->
            settings[taskStatusPreference] = taskStatus.intValue
        }
    }
}