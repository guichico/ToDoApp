package com.apphico.core_repository.calendar.task

import android.util.Log
import androidx.room.withTransaction
import com.apphico.core_model.RecurringTask
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import com.apphico.core_repository.calendar.room.entities.toCheckListItemDB
import com.apphico.core_repository.calendar.room.entities.toLocationDB
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.extensions.getNowDate
import java.time.LocalDate

interface TaskRepository {
    suspend fun insertTask(task: Task): Boolean
    suspend fun updateTask(task: Task, recurringTask: RecurringTask, initialTaskStartDate: LocalDate?): Boolean
    suspend fun deleteTask(task: Task, recurringTask: RecurringTask): Boolean
}

class TaskRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val alarmHelper: AlarmHelper,
    private val taskDao: TaskDao,
    private val taskDeletedDao: TaskDeletedDao,
    private val locationDao: LocationDao,
    private val checkListItemDao: CheckListItemDao
) : TaskRepository {

    override suspend fun insertTask(task: Task): Boolean {
        return try {
            appDatabase.withTransaction {
                val taskId = taskDao.insert(task.toTaskDB())

                task.location?.let {
                    locationDao.insert(it.toLocationDB(taskId))
                }

                checkListItemDao
                    .insertAll(task.checkList.map { it.toCheckListItemDB(taskId) })

                val insertedTask = task.copy(id = taskId)
                alarmHelper.setAlarm(insertedTask)
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateTask(task: Task, recurringTask: RecurringTask, initialTaskStartDate: LocalDate?): Boolean {
        return try {
            appDatabase.withTransaction {
                if (task.isRepeatable()) {
                    when (recurringTask) {
                        RecurringTask.All -> task.updateTask()

                        RecurringTask.Future -> {
                            taskDao.updateEndDate(task.id, task.startDate?.minusDays(1))
                            insertTask(task.copy(id = 0))
                        }

                        RecurringTask.ThisTask -> {
                            Task(id = task.id, startDate = initialTaskStartDate)
                                .insertTaskDeleted()

                            val endDate = task.endDate ?: task.startDate
                            insertTask(task.copy(id = 0, endDate = endDate))
                        }
                    }
                } else {
                    task.updateTask()
                }
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    private suspend fun Task.updateTask() {
        appDatabase.withTransaction {
            taskDao.update(this.toTaskDB())

            this.location?.let {
                locationDao.insertOrUpdate(it.toLocationDB(this.id))
            } ?: locationDao.delete(this.id)

            checkListItemDao.deleteAll(this.id, this.checkList.map { it.id })
            checkListItemDao.insertAll(this.checkList.filter { it.id == 0L }.map { it.toCheckListItemDB(this.id) })
            checkListItemDao.updateAll(this.checkList.filter { it.id != 0L }.map { it.toCheckListItemDB(this.id) })
        }
    }

    override suspend fun deleteTask(task: Task, recurringTask: RecurringTask): Boolean {
        return try {
            if (task.isRepeatable()) {
                when (recurringTask) {
                    RecurringTask.All -> taskDao.delete(task.toTaskDB())

                    RecurringTask.Future -> {
                        taskDao.updateEndDate(task.id, task.startDate?.minusDays(1))
                        task.insertTaskDeleted()
                    }

                    RecurringTask.ThisTask -> task.insertTaskDeleted()
                }
            } else {
                taskDao.delete(task.toTaskDB())
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    private suspend fun Task.insertTaskDeleted() {
        taskDeletedDao.insert(
            TaskDeletedDB(
                taskDeleteId = this.id,
                deletedDate = getNowDate(),
                taskDate = this.startDate
            )
        )
    }
}