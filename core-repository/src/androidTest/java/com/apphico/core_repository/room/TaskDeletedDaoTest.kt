package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
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
class TaskDeletedDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var taskDao: TaskDao
    private lateinit var taskDeletedDao: TaskDeletedDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        taskDao = db.taskDao()
        taskDeletedDao = db.taskDeletedDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteFutureTask() {
        runBlocking {
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
}