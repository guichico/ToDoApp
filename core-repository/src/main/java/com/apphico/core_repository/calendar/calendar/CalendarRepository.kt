package com.apphico.core_repository.calendar.calendar

import android.util.Log
import com.apphico.core_model.Group
import com.apphico.core_model.Task
import com.apphico.core_model.TaskStatus
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
import java.util.stream.Stream

interface CalendarRepository {
    fun getFromDay(date: LocalDate, status: TaskStatus, groups: List<Group>): Flow<List<Task>>
    fun getAll(fromStartDate: LocalDate, status: TaskStatus, groups: List<Group>): Flow<List<Task>>
    suspend fun changeTaskDone(task: Task, isDone: Boolean): Boolean
}

class CalendarRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskDoneDao: TaskDoneDao
) : CalendarRepository {

    override fun getFromDay(date: LocalDate, status: TaskStatus, groups: List<Group>): Flow<List<Task>> =
        taskDao.getFromDay(
            date = date,
            statusAllFlag = status == TaskStatus.ALL,
            statusDoneFlag = status == TaskStatus.DONE,
            statusUndoneFlag = status == TaskStatus.UNDONE,
            nullableGroupIdsFlag = groups.isEmpty(),
            groupIds = groups.map { it.id }
        )
            .map {
                it.map {
                    val task = it.toTask()
                    task.startDate?.let {
                        task.copy(startDate = date)
                    } ?: task
                }
            }

    override fun getAll(fromStartDate: LocalDate, status: TaskStatus, groups: List<Group>): Flow<List<Task>> =
        taskDao.getAllTasks(
            fromStartDate = fromStartDate,
            nullableGroupIdsFlag = groups.isEmpty(),
            groupIds = groups.map { it.id }
        )
            .map { it.map { it.toTask() } }
            .map { tasks ->
                mutableListOf<Task>()
                    .apply {
                        addAll(tasks.filter { it.startDate == null }.filterStatus(status))

                        tasks.filter { it.startDate != null }
                            .forEach { task -> addAll(task.addFutureTasks(fromStartDate, status)) }

                        sortBy { task ->
                            task.startDate?.let {
                                LocalDateTime.of(it, task.startTime ?: it.atStartOfDay().toLocalTime())
                            }
                        }
                    }
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

    private fun List<Task>.filterStatus(status: TaskStatus): List<Task> =
        this.filter { task ->
            when (status) {
                TaskStatus.ALL -> true
                TaskStatus.DONE -> task.isDone()
                TaskStatus.UNDONE -> !task.isDone()
            }
        }

    private fun Stream<Task>.filterStatus(status: TaskStatus): List<Task> = this.toList().filterStatus(status)

    private fun Task.addFutureTasks(
        selectedDate: LocalDate,
        status: TaskStatus
    ): List<Task> {
        val startDate = this.startDate
        // TODO Check how long to view
        val endDate = (this.endDate ?: selectedDate.plusYears(1)).plusDays(1)

        return if (startDate != null && selectedDate < endDate) {
            val beginShowDate = if (selectedDate > startDate) selectedDate else startDate

            beginShowDate.datesUntil(endDate)
                .filter {
                    val allDays = DayOfWeek.entries.map { it.getInt() }
                    val taskDaysOfWeek = if (this.daysOfWeek.isEmpty()) allDays else this.daysOfWeek

                    it.dayOfWeek.getInt() in taskDaysOfWeek
                }
                .map { newDate -> this.copy(startDate = newDate, isSaved = false) }
                .filterStatus(status)
        } else emptyList()
    }
}