package com.apphico.core_repository.calendar.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey

class AppSettingsDataStore(
    context: Context,
    dataStoreFileName: String
) : BaseDataStore(context, dataStoreFileName) {

    private val wasWelcomeClosedPreference = booleanPreferencesKey("was_welcome_closed")
    val wasWelcomeClosed = wasWelcomeClosedPreference.asBooleanFlow()

    suspend fun setWasWelcomeClosed(value: Boolean) = setValue(wasWelcomeClosedPreference, value)

    private val wasDatesExplanationClosedPreference = booleanPreferencesKey("was_dates_explanation_closed")
    val wasDatesExplanationClosed = wasDatesExplanationClosedPreference.asBooleanFlow()

    suspend fun setWasDatesExplanationClosed(value: Boolean) = setValue(wasDatesExplanationClosedPreference, value)
}