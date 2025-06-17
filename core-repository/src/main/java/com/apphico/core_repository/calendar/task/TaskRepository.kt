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
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.extensions.getInt
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowDateTime
import com.apphico.extensions.isAfterRightNotNull
import java.time.LocalDate
import java.time.LocalDateTime

interface TaskRepository {
    suspend fun getTask(taskId: Long): Task
    suspend fun insertTask(task: Task): Boolean
    suspend fun copyTask(task: Task, taskName: String = task.name): Boolean
    suspend fun updateTask(task: Task, recurringTask: RecurringTask, initialTaskStartDate: LocalDate?): Boolean
    suspend fun deleteTask(task: Task, recurringTask: RecurringTask): Boolean
    suspend fun setAlarm(taskId: Long): Long
    suspend fun setNextAlarm(taskId: Long): Long
    suspend fun setAlarmsAfterReboot()
}

class TaskRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val alarmHelper: AlarmHelper,
    private val taskDao: TaskDao,
    private val taskDeletedDao: TaskDeletedDao,
    private val locationDao: LocationDao,
    private val checkListItemDao: CheckListItemDao
) : TaskRepository {

    override suspend fun getTask(taskId: Long) = taskDao.getTask(taskId).toTask()

    override suspend fun insertTask(task: Task): Boolean {
        return try {
            appDatabase.withTransaction {
                val toBeSavedTask = task.checkDaysOfWeek()

                val taskId = taskDao.insert(toBeSavedTask.toTaskDB())

                toBeSavedTask.location?.let {
                    locationDao.insert(it.toLocationDB(taskId))
                }

                checkListItemDao
                    .insertAll(toBeSavedTask.checkList.map { it.toCheckListItemDB(taskId = taskId) })

                setAlarm(taskId)
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun copyTask(task: Task, taskName: String): Boolean {
        val copiedTask = task.copy(
            id = 0,
            name = taskName,
            checkList = task.checkList.map { it.copy(id = 0) },
            location = task.location?.copy(id = 0)
        )

        return insertTask(copiedTask)
    }

    override suspend fun updateTask(
        task: Task,
        recurringTask: RecurringTask,
        initialStartDate: LocalDate?
    ): Boolean {
        return try {
            appDatabase.withTransaction {
                val toBeSavedTask = task.checkDaysOfWeek()

                if (toBeSavedTask.isRepeatable()) {
                    when (recurringTask) {
                        RecurringTask.All -> {
                            val startDate = getTask(toBeSavedTask.id).startDate
                            toBeSavedTask.copy(startDate = startDate).updateTask()
                        }

                        RecurringTask.Future -> {
                            taskDao.updateEndDate(toBeSavedTask.id, toBeSavedTask.startDate?.minusDays(1))
                            copyTask(toBeSavedTask)
                        }

                        RecurringTask.ThisTask -> {
                            Task(id = toBeSavedTask.id, startDate = initialStartDate)
                                .insertTaskDeleted()

                            val endDate = toBeSavedTask.endDate ?: toBeSavedTask.startDate
                            copyTask(toBeSavedTask.copy(endDate = endDate, daysOfWeek = emptyList()))
                        }
                    }
                } else {
                    toBeSavedTask.updateTask()
                }

                alarmHelper.cancelAlarm(toBeSavedTask.reminderId)
                setAlarm(toBeSavedTask.id)
            }

            return true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun deleteTask(task: Task, recurringTask: RecurringTask): Boolean {
        return try {
            alarmHelper.cancelAlarm(task.reminderId)

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

    override suspend fun setAlarm(taskId: Long): Long = setAlarm(0, taskId)

    override suspend fun setNextAlarm(taskId: Long): Long = setAlarm(1, taskId)

    override suspend fun setAlarmsAfterReboot() =
        taskDao.getIdsWithReminders()
            .forEach { taskId ->
                setAlarm(taskId)
                Log.d("TEST", "taskId: $taskId")
            }

    private fun Task.checkDaysOfWeek(): Task {
        if (this.daysOfWeek.isEmpty() &&
            ((this.endDate == null && this.startDate != null) || (this.endDate != null && this.endDate.isAfterRightNotNull(this.startDate)))
        ) {
            return this.copy(daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7))
        }

        return this
    }

    private suspend fun Task.updateTask() {
        appDatabase.withTransaction {
            taskDao.update(this.toTaskDB())

            this.location?.let {
                locationDao.insertOrUpdate(it.toLocationDB(this.id))
            } ?: locationDao.delete(this.id)

            checkListItemDao.deleteAll(taskId = this.id, checkListItemIds = this.checkList.map { it.id })
            checkListItemDao.insertAll(this.checkList.filter { it.id == 0L }.map { it.toCheckListItemDB(taskId = this.id) })
            checkListItemDao.updateAll(this.checkList.filter { it.id != 0L }.map { it.toCheckListItemDB(taskId = this.id) })
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

    private suspend fun setAlarm(startDay: Int, taskId: Long): Long {
        val task = getTask(taskId)
        var alarmId = task.key()

        if (task.isRepeatable()) {
            var nextAlarmDate: LocalDate? = null

            val startDate = with(LocalDateTime.of(task.startDate, task.startTime)) {
                task.reminderDateTime(
                    (getNowDateTime().takeIf { it.isAfter(this) } ?: this).toLocalDate()
                ) ?: getNowDateTime()
            }

            for (days in startDay..7) {
                val nextDate = startDate.plusDays(days.toLong())
                val nextDayOfWeek = nextDate.dayOfWeek.getInt()

                if (task.daysOfWeek.contains(nextDayOfWeek)) {
                    alarmId = task.key() + nextDayOfWeek

                    if (nextDate.isAfter(getNowDateTime())) {
                        nextAlarmDate = nextDate.toLocalDate()
                        break
                    }
                }
            }

            val endDate = task.endDate ?: nextAlarmDate
            if (nextAlarmDate != null && nextAlarmDate <= endDate) {
                task.reminderDateTime(nextAlarmDate).setAlarm(alarmId, task)
            }
        } else {
            task.reminderDateTime().setAlarm(alarmId, task)
        }

        return alarmId
    }

    private suspend fun LocalDateTime?.setAlarm(alarmId: Long, task: Task) {
        this?.let { reminderDateTime ->
            if (reminderDateTime >= getNowDateTime()) {
                alarmHelper.setAlarm(alarmId, reminderDateTime, task.copy(startDate = reminderDateTime.toLocalDate()))
                taskDao.update(task.copy(reminderId = alarmId).toTaskDB())
            }
        } ?: run {
            taskDao.update(task.copy(reminderId = 0L).toTaskDB())
        }
    }
}