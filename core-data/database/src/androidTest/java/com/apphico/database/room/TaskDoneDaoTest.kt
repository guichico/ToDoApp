package com.apphico.database.room

import com.apphico.core_model.Task
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.dao.TaskDoneDao
import com.apphico.database.room.entities.TaskDoneDB
import com.apphico.database.room.entities.toTask
import com.apphico.database.room.entities.toTaskDB
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