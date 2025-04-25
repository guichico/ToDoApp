package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Status
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.dao.TaskDeletedDao
import com.apphico.core_repository.calendar.room.dao.TaskDoneDao
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import com.apphico.core_repository.calendar.room.entities.TaskDeletedDB
import com.apphico.core_repository.calendar.room.entities.TaskDoneDB
import com.apphico.core_repository.calendar.room.entities.toTask
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.core_repository.utils.sampleGroup
import com.apphico.core_repository.utils.sampleLocation
import com.apphico.core_repository.utils.sampleTask
import com.apphico.core_repository.utils.sampleTaskCheckList
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var groupDao: GroupDao
    private lateinit var taskDao: TaskDao
    private lateinit var taskDoneDao: TaskDoneDao
    private lateinit var taskDeletedDao: TaskDeletedDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var checkListItemDoneDao: CheckListItemDoneDao
    private lateinit var locationDao: LocationDao

    private var groupId: Long = 0
    private lateinit var fromDate: LocalDate
    private lateinit var insertedTask: Task

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        groupDao = db.groupDao()
        taskDao = db.taskDao()
        taskDoneDao = db.taskDoneDao()
        taskDeletedDao = db.taskDeletedDao()
        checkListItemDao = db.checkListItemDao()
        checkListItemDoneDao = db.checkListItemDoneDao()
        locationDao = db.locationDao()

        fromDate = getNowDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

        runBlocking {
            groupId = groupDao.insert(sampleGroup())
            insertedTask = insertTask(groupId)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndReadInList() = runTest {
        assert(getAllTasks().contains(insertedTask))
        assert(getDayTasks(fromDate).contains(insertedTask))
    }

    @Test
    @Throws(Exception::class)
    fun testFilters() = runTest {
        insertTask(endDate = fromDate.plusDays(1))

        val sunday = fromDate.minusDays(1)
        val tuesday = fromDate.plusDays(1)
        val wednesday = fromDate.plusDays(2)

        assert(getAllTasks(sunday, true, emptyList()).size == 2)
        assert(getAllTasks(wednesday, true, emptyList()).size == 1)
        assert(getAllTasks(fromDate, false, listOf(groupId)).size == 1)
        assert(getAllTasks(fromDate, false, listOf(Long.MAX_VALUE)).isEmpty())

        assert(getDayTasks(sunday).isEmpty())
        assert(getDayTasks(tuesday).isEmpty())
        assert(getDayTasks(wednesday, Status.ALL).size == 1)
        assert(getDayTasks(wednesday, Status.DONE).isEmpty())
        assert(getDayTasks(wednesday, Status.UNDONE).size == 1)
        assert(getDayTasks(date = wednesday, groupIds = listOf(groupId)).size == 1)
        assert(getDayTasks(date = wednesday, groupIds = listOf(Long.MAX_VALUE)).isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun update() = runTest {
        assert(getAllTasks().contains(insertedTask))

        val name = "Task updated"
        val description = "description updated"
        val endDate = getNowDate().plusMonths(1)
        val endTime = getNowTime()

        val updatedTask = insertedTask
            .copy(
                name = name,
                description = description,
                endDate = endDate,
                endTime = endTime
            )

        taskDao.update(updatedTask.toTaskDB())

        val allTasks = getAllTasks()

        assert(!allTasks.contains(insertedTask))
        assert(allTasks.contains(updatedTask))

        with(allTasks[0]) {
            assert(name == name)
            assert(description == description)
            assert(endDate == endDate)
            assert(endTime == endTime)
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() = runTest {
        assert(getAllTasks().contains(insertedTask))

        taskDao.delete(insertedTask.toTaskDB())

        assert(!getAllTasks().contains(insertedTask))

        assert(groupDao.getAll().first().size == 1)
        assert(taskDoneDao.getAll().first().isEmpty())
        assert(taskDeletedDao.getAll().first().isEmpty())
        assert(checkListItemDao.getAll().first().isEmpty())
        assert(checkListItemDoneDao.getAll().first().isEmpty())
        assert(locationDao.getAll().first().isEmpty())
    }

    private suspend fun insertTask(
        groupId: Long? = null,
        endDate: LocalDate? = null
    ): Task {
        val task = sampleTask(groupId, endDate)
        val taskId = taskDao.insert(task)

        val checkListIds = checkListItemDao.insertAll(sampleTaskCheckList(taskId))
        checkListItemDoneDao
            .insert(CheckListItemDoneDB(checkListItemDoneId = checkListIds[0], doneDate = getNowDate(), parentDate = task.startDate))

        locationDao.insert(sampleLocation(taskId))

        taskDoneDao.insert(TaskDoneDB(taskDoneId = taskId, doneDate = getNowDate(), taskDate = task.startDate))
        taskDeletedDao.insert(TaskDeletedDB(taskDeleteId = taskId, deletedDate = getNowDate().plusDays(1), taskDate = task.startDate?.plusDays(1)))

        return taskDao.getTask(taskId).toTask()
    }

    private suspend fun getAllTasks(
        fromStartDate: LocalDate = getNowDate(),
        nullableGroupIdsFlag: Boolean = true,
        groupIds: List<Long> = emptyList<Long>()
    ) = taskDao.getAllTasks(fromStartDate, nullableGroupIdsFlag, groupIds).first().map { it.toTask() }

    private suspend fun getDayTasks(date: LocalDate = getNowDate(), status: Status = Status.ALL, groupIds: List<Long> = emptyList()) =
        taskDao.getFromDay(
            date = date,
            statusAllFlag = status == Status.ALL,
            statusDoneFlag = status == Status.DONE,
            statusUndoneFlag = status == Status.UNDONE,
            nullableGroupIdsFlag = groupIds.isEmpty(),
            groupIds = groupIds
        )
            .first()
            .map { it.toTask() }
}