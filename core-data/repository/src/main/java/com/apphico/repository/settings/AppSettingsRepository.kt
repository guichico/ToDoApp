package com.apphico.repository.settings

import com.apphico.datastore.AppSettingsDataStore
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    fun wasWelcomeClosed(): Flow<Boolean>
    suspend fun setWasWelcomeClosed(value: Boolean)
    fun wasDatesExplanationClosed(): Flow<Boolean>
    suspend fun setWasDatesExplanationClosed(value: Boolean)
}

class AppSettingsRepositoryImpl(
    private val appSettingsDataStore: AppSettingsDataStore
) : AppSettingsRepository {
    override fun wasWelcomeClosed(): Flow<Boolean> = appSettingsDataStore.wasWelcomeClosed
    override suspend fun setWasWelcomeClosed(value: Boolean) {
        appSettingsDataStore.setWasWelcomeClosed(value)
    }

    override fun wasDatesExplanationClosed(): Flow<Boolean> = appSettingsDataStore.wasDatesExplanationClosed
    override suspend fun setWasDatesExplanationClosed(value: Boolean) {
        appSettingsDataStore.setWasDatesExplanationClosed(value)
    }
}