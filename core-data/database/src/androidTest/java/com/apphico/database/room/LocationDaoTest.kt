package com.apphico.database.room

import com.apphico.database.room.dao.LocationDao
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.utils.sampleLocation
import com.apphico.database.utils.sampleTask
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