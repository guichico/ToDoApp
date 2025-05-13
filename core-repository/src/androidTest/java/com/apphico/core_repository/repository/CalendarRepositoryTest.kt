package com.apphico.core_repository.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.core_repository.calendar.calendar.CalendarRepository
import com.apphico.core_repository.calendar.calendar.CalendarRepositoryImpl
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.ReminderIdDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.core_repository.calendar.task.TaskRepositoryImpl
import com.apphico.core_repository.utils.sampleGroup
import com.apphico.extensions.getNowDate
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
    private lateinit var reminderIdDao: ReminderIdDao

    private lateinit var calendarRepository: CalendarRepository
    private lateinit var taskRepository: TaskRepository

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
        reminderIdDao = db.reminderIdDao()

        calendarRepository = CalendarRepositoryImpl(
            taskDao = taskDao,
            taskDoneDao = taskDoneDao
        )

        taskRepository = TaskRepositoryImpl(
            appDatabase = db,
            taskDao = taskDao,
            taskDeletedDao = taskDeletedDao,
            locationDao = locationDao,
            checkListItemDao = checkListItemDao,
            reminderIdDao = reminderIdDao,
            alarmHelper = mock<AlarmHelper> {},
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