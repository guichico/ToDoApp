package com.apphico.core_repository.calendar.calendar

import android.util.Log
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

interface CalendarRepository {
    fun getAll(fromStartDate: LocalDate): Flow<List<Task>>
    fun getFromDay(date: LocalDate): Flow<List<Task>>
}

class CalendarRepositoryImpl(
    private val taskDao: TaskDao
) : CalendarRepository {

    override fun getAll(fromStartDate: LocalDate): Flow<List<Task>> {
        Log.d(CalendarRepository::class.simpleName, "getAll")
        return taskDao.getAll(fromStartDate)
            .map { it.map { it.toTask() } }
    }


    override fun getFromDay(date: LocalDate): Flow<List<Task>> {
        Log.d(CalendarRepository::class.simpleName, "getFromDay")
        return taskDao.getFromDay(date)
            .map { it.map { it.toTask() } }
    }
}