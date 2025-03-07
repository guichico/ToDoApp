package com.apphico.core_repository.calendar.task

import android.util.Log
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.toCheckListItemDB
import com.apphico.core_repository.calendar.room.toLocationDB
import com.apphico.core_repository.calendar.room.toTaskDB

interface TaskRepository {
    suspend fun insertTask(task: Task): Boolean
    suspend fun deleteTask(task: Task): Boolean
}

class TaskRepositoryImpl(
    val appDatabase: AppDatabase
) : TaskRepository {

    override suspend fun insertTask(task: Task): Boolean {
        return try {
            val taskId = appDatabase.taskDao()
                .insert(task.toTaskDB())

            task.location?.let {
                appDatabase.locationDao()
                    .insert(it.toLocationDB(taskId))
            }

            appDatabase.checkListItemDao()
                .insertAll(task.checkList.map { it.toCheckListItemDB(taskId) })

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun deleteTask(task: Task): Boolean {
        return try {
            appDatabase.taskDao().delete(task.toTaskDB())

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }
}