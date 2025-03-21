package com.apphico.core_repository.calendar.calendar

import android.util.Log
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.extensions.getInt
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

interface CalendarRepository {
    fun getAll(fromStartDate: LocalDate): Flow<List<Task>>
    fun getFromDay(date: LocalDate): Flow<List<Task>>
    suspend fun changeTaskDone(task: Task, isDone: Boolean): Boolean
}

class CalendarRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskDoneDao: TaskDoneDao
) : CalendarRepository {

    override fun getAll(fromStartDate: LocalDate): Flow<List<Task>> {
        Log.d(CalendarRepository::class.simpleName, "getAll")

        return taskDao.getAll(fromStartDate)
            .map { it.map { it.toTask() } }
            .map { tasks ->
                mutableListOf<Task>()
                    .apply {
                        addAll(tasks.filter { it.startDate == null })

                        tasks.filter { it.startDate != null }
                            .forEach { task -> addAll(task.addFutureTasks(fromStartDate)) }

                        sortBy {
                            if (it.startDate != null) {
                                LocalDateTime.of(it.startDate, it.startTime ?: it.startDate?.atStartOfDay()?.toLocalTime())
                            } else null
                        }
                    }
            }
    }

    private fun Task.addFutureTasks(
        selectedDate: LocalDate
    ): List<Task> {
        val startDate = this.startDate
        // TODO Check how long to view
        val endDate = (this.endDate ?: selectedDate.plusYears(1)).plusDays(1)

        return if (startDate != null && selectedDate < endDate) {
            selectedDate.datesUntil(endDate)
                .filter {
                    val allDays = DayOfWeek.entries.map { it.getInt() }
                    val taskDaysOfWeek = if (this.daysOfWeek.isEmpty()) allDays else this.daysOfWeek

                    it.dayOfWeek.getInt() in taskDaysOfWeek
                }
                .map { newDate -> this.copy(startDate = newDate, isSaved = false) }
                .toList()
        } else emptyList()
    }

    override fun getFromDay(date: LocalDate): Flow<List<Task>> {
        Log.d(CalendarRepository::class.simpleName, "getFromDay")
        return taskDao.getFromDay(date)
            .map { it.map { it.toTask() } }
    }

    override suspend fun changeTaskDone(task: Task, isDone: Boolean): Boolean {
        return try {
            if (isDone) {
                taskDoneDao.insert(TaskDoneDB(taskDoneId = task.id, doneDate = getNowDate(), taskDate = task.startDate))
            } else {
                taskDoneDao.delete(task.id, task.startDate)
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

}