package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskDoneDaoTest {

    private lateinit var appContext: Context
    private lateinit var db: AppDatabase

    private lateinit var taskDao: TaskDao
    private lateinit var taskDoneDao: TaskDoneDao

    @Before
    fun createDb() {
        appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        taskDao = db.taskDao()
        taskDoneDao = db.taskDoneDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDoneChanged() {
        runBlocking {
            val taskId = taskDao.insert(Task(name = "Test task").toTaskDB())
            var insertedTask = taskDao.getTask(taskId)

            assert(!insertedTask.toTask().isDone())

            taskDoneDao.insert(TaskDoneDB(taskDoneId = taskId, doneDate = getNowDate(), taskDate = null))

            insertedTask = taskDao.getTask(taskId)

            assert(insertedTask.toTask().isDone())

            taskDoneDao.delete(taskId, null)

            insertedTask = taskDao.getTask(taskId)

            assert(!insertedTask.toTask().isDone())
        }
    }
}