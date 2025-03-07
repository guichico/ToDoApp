package com.apphico.core_repository.calendar.calendar

import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CalendarRepository {
    fun getCalendar(): Flow<List<Task>>
}

class CalendarRepositoryImpl(
    val appDatabase: AppDatabase
) : CalendarRepository {

    override fun getCalendar(): Flow<List<Task>> =
        flow {
            emit(
                appDatabase.taskDao()
                    .getAll()
                    .map { it.toTask() }
            )
        }
}