package com.apphico.core_repository.calendar.task

import android.util.Log
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.toCheckListItemDB
import com.apphico.core_repository.calendar.room.entities.toLocationDB
import com.apphico.core_repository.calendar.room.entities.toTaskDB

interface TaskRepository {
    suspend fun insertTask(task: Task): Boolean
    suspend fun updateTask(task: Task): Boolean
    suspend fun changeDone(task: Task): Boolean
    suspend fun deleteTask(task: Task): Boolean
}

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val locationDao: LocationDao,
    private val checkListItemDao: CheckListItemDao,
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

            task.location?.let {
                locationDao.update(it.toLocationDB(task.id))
            }

            checkListItemDao.deleteAll(task.id)
            checkListItemDao.insertAll(task.checkList.map { it.toCheckListItemDB(task.id) })

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun changeDone(task: Task): Boolean {
        return try {
            taskDao.update(task.toTaskDB())
            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun deleteTask(task: Task): Boolean {
        return try {
            taskDao.delete(task.toTaskDB())
            locationDao.delete(task.id)
            checkListItemDao.deleteAll(task.id)

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }
}