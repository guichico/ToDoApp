package com.apphico.core_repository.calendar.task

import android.util.Log
import com.apphico.core_model.RecurringTask
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
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
    private val taskDao: TaskDao,
    private val taskDeletedDao: TaskDeletedDao,
    private val locationDao: LocationDao,
    private val checkListItemDao: CheckListItemDao,
    private val checkListItemDoneDao: CheckListItemDoneDao
) : TaskRepository {

    override suspend fun insertTask(task: Task): Boolean {
        return try {
            val taskId = taskDao.insert(task.toTaskDB())

            task.location?.let {
                locationDao
                    .insert(it.toLocationDB(taskId))
            }

            checkListItemDao
                .insertAll(task.checkList.map { it.toCheckListItemDB(taskId) })

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        return try {
            taskDao.update(task.toTaskDB())

            locationDao.delete(task.id)
            task.location?.let {
                locationDao.insert(it.toLocationDB(task.id))
            }

            checkListItemDao.deleteAll(task.id)
            checkListItemDao.insertAll(task.checkList.map { it.toCheckListItemDB(task.id) })

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
                    RecurringTask.All -> task.deleteAllTask()

                    RecurringTask.Future -> {
                        taskDao.updateEndDate(task.id, task.startDate?.minusDays(1))
                        task.insertTaskDeleted()
                    }

                    RecurringTask.ThisTask -> task.insertTaskDeleted()
                }
            } else {
                task.deleteAllTask()
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

    private suspend fun Task.deleteAllTask() {
        taskDao.delete(this.toTaskDB())
        taskDeletedDao.deleteAll(this.id)
        locationDao.delete(this.id)
        checkListItemDao.deleteAll(this.id)
        checkListItemDoneDao.deleteAll(this.checkList.map { it.id })
    }
}