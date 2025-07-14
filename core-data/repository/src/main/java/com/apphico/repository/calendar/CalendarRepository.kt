package com.apphico.repository.calendar

import android.util.Log
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.core_model.sortByStartDate
import com.apphico.core_model.toCalendarOrder
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.dao.TaskDoneDao
import com.apphico.database.room.entities.TaskDoneDB
import com.apphico.database.room.entities.toTask
import com.apphico.extensions.getNowDate
import com.apphico.repository.task.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate

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
            .map { tasks -> tasks.toCalendarOrder(fromStartDate, status) }
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
}
