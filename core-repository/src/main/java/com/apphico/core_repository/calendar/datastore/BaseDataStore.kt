package com.apphico.core_repository.calendar.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseDataStore(
    dataStoreFileName: String
) {
    open val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreFileName)
}

