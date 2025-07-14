package com.apphico.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseDataStore(
    protected val context: Context,
    dataStoreFileName: String
) {
    open val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreFileName)

    protected fun Preferences.Key<Boolean>.asBooleanFlow(): Flow<Boolean> =
        context.dataStore.data.map { preferences -> preferences[this] == true }

    protected fun Preferences.Key<Int>.asIntFlow(): Flow<Int?> =
        context.dataStore.data.map { preferences -> preferences[this] }

    protected suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }
}

