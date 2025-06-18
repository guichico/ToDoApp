package com.apphico.core_repository.calendar.datastore

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.Status
import kotlinx.coroutines.flow.Flow

class UserSettingsDataStore(
    context: Context,
    dataStoreFileName: String
) : BaseDataStore(context, dataStoreFileName) {

    private val viewModePreference = intPreferencesKey("view_mode")
    val viewMode = viewModePreference.asCalendarViewModeFlow()

    suspend fun setCalendarViewMode(calendarViewMode: CalendarViewMode) = setValue(viewModePreference, calendarViewMode.intValue)

    private val taskStatusPreference = intPreferencesKey("task_status")
    val taskStatus: Flow<Status> = taskStatusPreference.asStatusFlow()

    suspend fun setTaskStatus(taskStatus: Status) = setValue(taskStatusPreference, taskStatus.intValue)

    private val achievementStatusPreference = intPreferencesKey("achievement_status")
    val achievementStatus: Flow<Status> = achievementStatusPreference.asStatusFlow()

    suspend fun setAchievementStatus(achievementStatus: Status) = setValue(achievementStatusPreference, achievementStatus.intValue)
}