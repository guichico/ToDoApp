package com.apphico.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.database.room.AppDatabase
import com.apphico.database.room.dao.CheckListItemDao
import com.apphico.database.room.dao.GroupDao
import com.apphico.database.room.dao.LocationDao
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.dao.TaskDeletedDao
import com.apphico.database.room.dao.TaskDoneDao
import com.apphico.database.room.entities.GroupDB
import com.apphico.extensions.getNowDate
import com.apphico.repository.alarm.AlarmHelper
import com.apphico.repository.calendar.CalendarRepository
import com.apphico.repository.calendar.CalendarRepositoryImpl
import com.apphico.repository.task.TaskRepository
import com.apphico.repository.task.TaskRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class CalendarRepositoryTest {

    private lateinit var db: AppDatabase

    private lateinit var groupDao: GroupDao
    private lateinit var taskDao: TaskDao
    private lateinit var taskDoneDao: TaskDoneDao
    private lateinit var taskDeletedDao: TaskDeletedDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var locationDao: LocationDao

    private lateinit var calendarRepository: CalendarRepository
    private lateinit var taskRepository: TaskRepository

    fun sampleGroup() = GroupDB(name = "Group test", color = -7745552)

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        groupDao = db.groupDao()
        taskDao = db.taskDao()
        taskDoneDao = db.taskDoneDao()
        taskDeletedDao = db.taskDeletedDao()
        checkListItemDao = db.checkListItemDao()
        locationDao = db.locationDao()

        calendarRepository = CalendarRepositoryImpl(
            taskDao = taskDao,
            taskDoneDao = taskDoneDao
        )

        taskRepository = TaskRepositoryImpl(
            appDatabase = db,
            alarmHelper = mock<AlarmHelper> {},
            taskDao = taskDao,
            taskDeletedDao = taskDeletedDao,
            locationDao = locationDao,
            checkListItemDao = checkListItemDao
        )

        runBlocking {
            val groupIds = groupDao.insertAll(
                listOf(
                    sampleGroup(),
                    sampleGroup(),
                    sampleGroup(),
                    sampleGroup()
                )
            )

            taskRepository.insertTask(Task(name = ""))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getFromDay() {
        val dayTasks = calendarRepository.getFromDay(
            date = getNowDate(),
            status = Status.ALL,
            groups = emptyList()
        )
    }

    @Test
    @Throws(Exception::class)
    fun getAll() {
        val allTasks = calendarRepository.getAll(
            fromStartDate = getNowDate(),
            status = Status.ALL,
            groups = emptyList()
        )
    }

    @Test
    @Throws(Exception::class)
    fun changeTaskDone() {
        // calendarRepository.changeTaskDone()
    }
}