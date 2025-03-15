package com.apphico.todoapp

import android.app.Application
import com.apphico.core_repository.calendar.room.AppDatabase
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ToDoApp : Application() {

    @Inject
    lateinit var appDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}