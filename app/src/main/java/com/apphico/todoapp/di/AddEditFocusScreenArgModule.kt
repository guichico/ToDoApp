package com.apphico.todoapp.di

import androidx.lifecycle.SavedStateHandle
import com.apphico.core_model.FocusMode
import com.apphico.todoapp.focus.FOCUS_ARG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AddEditFocusScreenArgModule {

    annotation class FocusData

    @Provides
    @FocusData
    @ViewModelScoped
    fun provideFocus(savedStatedHandle: SavedStateHandle): FocusMode? = savedStatedHandle[FOCUS_ARG]

}