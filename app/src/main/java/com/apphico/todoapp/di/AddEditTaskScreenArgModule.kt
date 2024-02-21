package com.apphico.todoapp.di

import androidx.lifecycle.SavedStateHandle
import com.apphico.core_model.Task
import com.apphico.todoapp.task.TASK_ARG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AddEditTaskScreenArgModule {

    annotation class TaskData

    @Provides
    @TaskData
    @ViewModelScoped
    fun provideTask(savedStatedHandle: SavedStateHandle): Task? = savedStatedHandle[TASK_ARG]

}