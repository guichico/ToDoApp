package com.apphico.core_repository.calendar.calendar

import android.util.Log
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.extensions.getInt
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream

interface CalendarRepository {
    fun getFromDay(date: LocalDate, status: Status, groups: List<Group>): Flow<List<Task>>
    fun getAll(fromStartDate: LocalDate, status: Status, groups: List<Group>): Flow<List<Task>>
    suspend fun changeTaskDone(task: Task, isDone: Boolean): Boolean
}

class CalendarRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskDoneDao: TaskDoneDao
) : CalendarRepository {

    override fun getFromDay(date: LocalDate, status: Status, groups: List<Group>): Flow<List<Task>> =
        taskDao.getFromDay(
            date = date,
            statusAllFlag = status == Status.ALL,
            statusDoneFlag = status == Status.DONE,
            statusUndoneFlag = status == Status.UNDONE,
            nullableGroupIdsFlag = groups.isEmpty(),
            groupIds = groups.map { it.id }
        )
            .flowOn(Dispatchers.IO)
            .map {
                it.map { taskWithRelations ->
                    val task = taskWithRelations.toTask()
                    task.startDate?.let {
                        task.copy(startDate = date)
                    } ?: task
                }
                    // Use it to keep same sort of getAll
                    .sortByStartDate()
            }
            .flowOn(Dispatchers.Default)

    override fun getAll(fromStartDate: LocalDate, status: Status, groups: List<Group>): Flow<List<Task>> =
        taskDao.getAllTasks(
            fromStartDate = fromStartDate,
            nullableGroupIdsFlag = groups.isEmpty(),
            groupIds = groups.map { it.id }
        )
            .flowOn(Dispatchers.IO)
            .map { it.map { taskWithRelations -> taskWithRelations.toTask() } }
            .map { tasks ->
                mutableListOf<Task>()
                    .apply {
                        // One time tasks
                        addAll(tasks.filter { !it.isRepeatable() }.filterTasks(status))

                        // Recurring tasks
                        tasks.filter { it.isRepeatable() }
                            .forEach { task -> addAll(task.addFutureTasks(fromStartDate, status)) }
                    }
                    .sortByStartDate()
            }
            .flowOn(Dispatchers.Default)

    override suspend fun changeTaskDone(task: Task, isDone: Boolean): Boolean =
        try {
            if (isDone) {
                taskDoneDao.insert(TaskDoneDB(taskDoneId = task.id, doneDate = getNowDate(), taskDate = task.startDate))
            } else {
                taskDoneDao.delete(task.id, task.startDate)
            }

            true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            false
        }

    private fun List<Task>.filterTasks(status: Status): List<Task> =
        this.filter { task ->
            !task.isDeleted() && when (status) {
                Status.ALL -> true
                Status.DONE -> task.isDone()
                Status.UNDONE -> !task.isDone()
            }
        }

    private fun Stream<Task>.filterTasks(status: Status): List<Task> = this.toList().filterTasks(status)

    private fun List<Task>.sortByStartDate() =
        this.sortedWith(
            compareBy<Task> { task ->
                task.startDate != null
            }
                .thenBy { task ->
                    task.startDate?.let {
                        LocalDateTime.of(it, task.startTime ?: it.atStartOfDay().toLocalTime())
                    }
                }
                .thenBy(nullsLast()) { task ->
                    task.endDate?.let {
                        LocalDateTime.of(it, task.endTime ?: it.atStartOfDay().toLocalTime())
                    }
                }
                .thenBy { it.endTime }
                .thenBy { it.id }
        )

    private fun Task.addFutureTasks(
        selectedDate: LocalDate,
        status: Status
    ): List<Task> {
        val startDate = this.startDate
        // TODO Check how long to view
        val endDate = (this.endDate ?: selectedDate.plusYears(1)).plusDays(1)

        return if (startDate != null && selectedDate < endDate) {
            val beginShowDate = if (selectedDate > startDate) selectedDate else startDate

            beginShowDate.datesUntil(endDate)
                .filter {
                    val allDays = DayOfWeek.entries.map { daysOfWeek -> daysOfWeek.getInt() }
                    val taskDaysOfWeek = this.daysOfWeek.ifEmpty { allDays }

                    it.dayOfWeek.getInt() in taskDaysOfWeek
                }
                .map { newDate -> this.copy(startDate = newDate, isSaved = false) }
                .filterTasks(status)
        } else emptyList()
    }
}