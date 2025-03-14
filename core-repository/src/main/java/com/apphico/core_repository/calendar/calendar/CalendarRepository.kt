package com.apphico.core_repository.calendar.calendar

import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

interface CalendarRepository {
    fun getAll(fromStartDate: LocalDate): Flow<List<Task>>
    fun getFromDay(date: LocalDate): Flow<List<Task>>
}

class CalendarRepositoryImpl(
    val appDatabase: AppDatabase
) : CalendarRepository {

    override fun getAll(fromStartDate: LocalDate): Flow<List<Task>> =
        flow {
            emit(
                appDatabase.taskDao()
                    .getAll(fromStartDate)
                    .map { it.toTask() }
            )
        }

    override fun getFromDay(date: LocalDate): Flow<List<Task>> =
        flow {
            emit(
                appDatabase.taskDao()
                    .getFromDay(date)
                    .map { it.toTask() }
            )
        }
}