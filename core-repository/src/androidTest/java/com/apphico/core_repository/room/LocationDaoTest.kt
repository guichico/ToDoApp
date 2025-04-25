package com.apphico.core_repository.room

import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.utils.sampleLocation
import com.apphico.core_repository.utils.sampleTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocationDaoTest : BaseDaoTest() {
    private lateinit var taskDao: TaskDao
    private lateinit var locationDao: LocationDao

    @Before
    fun init() {
        taskDao = db.taskDao()
        locationDao = db.locationDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runTest {
        val taskId = taskDao.insert(sampleTask())

        locationDao.insert(sampleLocation(taskId))

        assert(locationDao.getAll().first().size == 1)

        locationDao.delete(taskId = taskId)

        assert(locationDao.getAll().first().isEmpty())
    }
}