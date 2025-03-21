package com.apphico.core_repository.calendar.calendar

import android.util.Log
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

interface CalendarRepository {
    fun getAll(fromStartDate: LocalDate): Flow<List<Task>>
    fun getFromDay(date: LocalDate): Flow<List<Task>>

    fun isTaskDone(task: Task): Flow<Boolean>
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
    }

    override fun getFromDay(date: LocalDate): Flow<List<Task>> {
        Log.d(CalendarRepository::class.simpleName, "getFromDay")
        return taskDao.getFromDay(date)
            .map { it.map { it.toTask() } }
    }

    override fun isTaskDone(task: Task): Flow<Boolean> =
        taskDoneDao.getDone(task.id, task.startDate!!)
            .map { it != null }

    override suspend fun changeTaskDone(task: Task, isDone: Boolean): Boolean {
        return try {
            if (isDone) {
                taskDoneDao.insert(TaskDoneDB(taskDoneId = task.id, doneDate = getNowDate(), taskDate = task.startDate))
            } else {
                taskDoneDao.delete(task.id, task.startDate!!)
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

}