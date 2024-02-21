package com.apphico.todoapp.di

import androidx.lifecycle.SavedStateHandle
import com.apphico.core_model.Location
import com.apphico.todoapp.task.LOCATION_ARG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AddEditLocationScreenArgModule {

    annotation class LocationData

    @Provides
    @LocationData
    @ViewModelScoped
    fun provideLocation(savedStatedHandle: SavedStateHandle): Location? = savedStatedHandle[LOCATION_ARG]

}