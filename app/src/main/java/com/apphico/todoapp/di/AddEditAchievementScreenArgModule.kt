package com.apphico.todoapp.di

import androidx.lifecycle.SavedStateHandle
import com.apphico.core_model.Achievement
import com.apphico.todoapp.achievements.ACHIEVEMENT_ARG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AddEditAchievementScreenArgModule {

    annotation class AchievementData

    @Provides
    @AchievementData
    @ViewModelScoped
    fun provideAchievement(savedStatedHandle: SavedStateHandle): Achievement? = savedStatedHandle[ACHIEVEMENT_ARG]

}