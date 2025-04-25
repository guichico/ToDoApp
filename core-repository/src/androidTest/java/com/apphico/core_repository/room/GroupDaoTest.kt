package com.apphico.core_repository.room

import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.utils.sampleGroup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GroupDaoTest : BaseDaoTest() {
    private lateinit var groupDao: GroupDao

    @Before
    fun init() {
        groupDao = db.groupDao()
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