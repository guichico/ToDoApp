package com.apphico.core_repository.calendar.task

import android.util.Log
import androidx.room.withTransaction
import com.apphico.core_model.RecurringTask
import com.apphico.core_model.Task
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

interface TaskRepository {
    suspend fun insertTask(task: Task): Boolean
    suspend fun updateTask(task: Task): Boolean
    suspend fun deleteTask(task: Task, recurringTask: RecurringTask): Boolean
}

class TaskRepositoryImpl(
    private val appDatabase: AppDatabase,
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
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        return try {
            appDatabase.withTransaction {
                taskDao.update(task.toTaskDB())

                task.location?.let {
                    locationDao.insertOrUpdate(it.toLocationDB(task.id))
                } ?: locationDao.delete(task.id)

                checkListItemDao.deleteAll(task.id, task.checkList.map { it.id })
                checkListItemDao.insertAll(task.checkList.filter { it.id == 0L }.map { it.toCheckListItemDB(task.id) })
                checkListItemDao.updateAll(task.checkList.filter { it.id != 0L }.map { it.toCheckListItemDB(task.id) })
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
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