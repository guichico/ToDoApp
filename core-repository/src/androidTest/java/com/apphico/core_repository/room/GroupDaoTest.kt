package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.utils.sampleGroup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GroupDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var groupDao: GroupDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        groupDao = db.groupDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runTest {
        groupDao.insert(sampleGroup())

        assert(groupDao.getAll().first().size == 1)
    }

    @Test
    @Throws(Exception::class)
    fun update() = runTest {
        val group = sampleGroup()

        val groupId = groupDao.insert(group)
        val insertedGroup = group.copy(groupId = groupId)

        assert(groupDao.getAll().first()[0] == insertedGroup)

        groupDao.update(insertedGroup.copy(name = "Testing group", color = -1886900))

        with(groupDao.getAll().first()[0]) {
            assert(name == "Testing group")
            assert(color == -1886900)
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() = runTest {
        val group = sampleGroup()

        val groupId = groupDao.insert(group)
        val insertedGroup = group.copy(groupId = groupId)

        assert(groupDao.getAll().first().size == 1)

        groupDao.delete(insertedGroup)

        assert(groupDao.getAll().first().isEmpty())
    }
}