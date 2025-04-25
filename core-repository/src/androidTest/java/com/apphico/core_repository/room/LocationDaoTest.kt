package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.LocationDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.utils.sampleLocation
import com.apphico.core_repository.utils.sampleTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LocationDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var taskDao: TaskDao
    private lateinit var locationDao: LocationDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        taskDao = db.taskDao()
        locationDao = db.locationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
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