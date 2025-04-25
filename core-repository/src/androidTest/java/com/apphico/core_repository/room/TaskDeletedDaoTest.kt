package com.apphico.core_repository.room

import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskDeletedDaoTest : BaseDaoTest() {
    private lateinit var taskDao: TaskDao
    private lateinit var taskDeletedDao: TaskDeletedDao

    @Before
    fun init() {
        taskDao = db.taskDao()
        taskDeletedDao = db.taskDeletedDao()
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteFutureTask() = runTest {
        val taskDate = getNowDate()
        val tomorrow = taskDate.plusDays(1)

        val taskId = taskDao.insert(Task(name = "Test task", startDate = taskDate).toTaskDB())
        var insertedTask = taskDao.getTask(taskId).toTask()

        assert(!insertedTask.isDeleted())

        taskDeletedDao.insert(TaskDeletedDB(taskDeleteId = taskId, deletedDate = tomorrow, taskDate = tomorrow))

        insertedTask = taskDao.getTask(taskId).toTask().copy(startDate = tomorrow)

        assert(insertedTask.isDeleted())
    }
}