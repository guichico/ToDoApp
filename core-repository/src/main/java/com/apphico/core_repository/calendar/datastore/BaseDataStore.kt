package com.apphico.core_repository.calendar.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.apphico.core_model.CalendarViewMode
import com.apphico.core_model.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseDataStore(
    protected val context: Context,
    dataStoreFileName: String
) {
    open val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreFileName)

    protected fun Preferences.Key<Boolean>.asFlow(): Flow<Boolean> =
        context.dataStore.data
            .map { preferences -> preferences[this] == true }

    protected fun Preferences.Key<Int>.asStatusFlow(): Flow<Status> =
        context.dataStore.data
            .map { preferences ->
                Status.entries.firstOrNull { it.intValue == preferences[this] } ?: Status.ALL
            }

    protected fun Preferences.Key<Int>.asCalendarViewModeFlow(): Flow<CalendarViewMode> =
        context.dataStore.data
            .map { preferences ->
                CalendarViewMode.entries.firstOrNull { it.intValue == preferences[this] } ?: CalendarViewMode.DAY
            }

    protected suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }
}

