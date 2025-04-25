package com.apphico.core_repository.room

import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskDoneDaoTest : BaseDaoTest() {
    private lateinit var taskDao: TaskDao
    private lateinit var taskDoneDao: TaskDoneDao

    @Before
    fun init() {
        taskDao = db.taskDao()
        taskDoneDao = db.taskDoneDao()
    }

    @Test
    @Throws(Exception::class)
    fun testDoneChanged() = runTest {
        val taskId = taskDao.insert(Task(name = "Test task").toTaskDB())
        var insertedTask = taskDao.getTask(taskId).toTask()

        assert(!insertedTask.isDone())

        taskDoneDao.insert(TaskDoneDB(taskDoneId = taskId, doneDate = getNowDate(), taskDate = null))

        insertedTask = taskDao.getTask(taskId).toTask()

        assert(insertedTask.isDone())

        taskDoneDao.delete(taskId, null)

        insertedTask = taskDao.getTask(taskId).toTask()

        assert(!insertedTask.isDone())
    }
}