package com.apphico.datastore.di

import android.content.Context
import com.apphico.datastore.AppSettingsDataStore
import com.apphico.datastore.UserSettingsDataStore
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
    fun provideAppSettingsDataStore(@ApplicationContext appContext: Context): AppSettingsDataStore =
        AppSettingsDataStore(appContext, "app_settings")

    @Provides
    @Singleton
    fun provideUserSettingsDataStore(@ApplicationContext appContext: Context): UserSettingsDataStore =
        UserSettingsDataStore(appContext, "user_settings")

}