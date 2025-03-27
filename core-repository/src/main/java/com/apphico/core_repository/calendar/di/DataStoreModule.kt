package com.apphico.core_repository.calendar.di

import android.content.Context
import com.apphico.core_repository.calendar.datastore.UserSettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule() {

    @Provides
    @Singleton
    fun provideUserSettingsDataStore(@ApplicationContext appContext: Context): UserSettingsDataStore =
        UserSettingsDataStore(appContext, "user_settings")

}