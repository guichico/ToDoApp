package com.apphico.database.room

import com.apphico.core_model.Achievement
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.Status
import com.apphico.core_model.filterStatus
import com.apphico.database.room.dao.AchievementDao
import com.apphico.database.room.dao.CheckListItemDao
import com.apphico.database.room.dao.CheckListItemDoneDao
import com.apphico.database.room.dao.GroupDao
import com.apphico.database.room.dao.ProgressDao
import com.apphico.database.room.entities.toAchievement
import com.apphico.database.room.entities.toAchievementDB
import com.apphico.database.utils.sampleAchievement
import com.apphico.database.utils.sampleGroup
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AchievementDaoTest : BaseDaoTest() {
    private lateinit var groupDao: GroupDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var checkListItemDoneDao: CheckListItemDoneDao
    private lateinit var progressDao: ProgressDao

    private var groupId: Long = 0

    @Before
    fun init() {
        groupDao = db.groupDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
        checkListItemDoneDao = db.checkListItemDoneDao()
        progressDao = db.progressDao()

        runBlocking {
            groupId = groupDao.insert(sampleGroup())
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndReadInList() = runTest {
        val achievementId = achievementDao.insert(sampleAchievement())
        val insertedAchievement = achievementDao.getAchievement(achievementId).toAchievement()

        assert(getAll().contains(insertedAchievement))
    }

    @Test
    @Throws(Exception::class)
    fun testFilters() = runTest {
        val achievementId1 = achievementDao.insert(sampleAchievement(groupId))
        val achievementId2 = achievementDao.insert(sampleAchievement(groupId, MeasurementType.Percentage().intValue))
        val achievementId3 = achievementDao.insert(sampleAchievement(groupId = groupId, doneDate = null))

        val insertedAchievement1 = achievementDao.getAchievement(achievementId1).toAchievement()
        val insertedAchievement2 = achievementDao.getAchievement(achievementId2).toAchievement()
        val insertedAchievement3 = achievementDao.getAchievement(achievementId3).toAchievement()

        val doneAchievements = getAll(Status.DONE)

        assert(doneAchievements.size == 2)
        assert(doneAchievements.containsAll(listOf(insertedAchievement1, insertedAchievement2)))

        val undoneAchievements = getAll(Status.UNDONE)

        assert(undoneAchievements.size == 1)
        assert(undoneAchievements.contains(insertedAchievement3))

        assert(getAll(groupIds = listOf(groupId)).size == 3)
        assert(getAll(groupIds = listOf(Long.MAX_VALUE)).isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun update() = runTest {
        val achievementId = achievementDao.insert(sampleAchievement())
        val insertedAchievement = achievementDao.getAchievement(achievementId).toAchievement()

        val name = "Achievement updated"
        val description = "description updated"
        val endDate = getNowDate().plusMonths(1)

        val updatedAchievement = insertedAchievement.copy(
            name = name,
            description = description,
            endDate = endDate,
            measurementType = MeasurementType.Percentage()
        )

        achievementDao.update(updatedAchievement.toAchievementDB())

        val allAchievements = getAll()

        assert(!allAchievements.contains(insertedAchievement))
        assert(allAchievements.contains(updatedAchievement))

        with(allAchievements[0]) {
            assert(name == name)
            assert(description == description)
            assert(endDate == endDate)
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() = runTest {
        val achievementId = achievementDao.insert(sampleAchievement())
        val insertedAchievement = achievementDao.getAchievement(achievementId).toAchievement()

        assert(getAll().contains(insertedAchievement))

        achievementDao.delete(insertedAchievement.toAchievementDB())

        assert(!getAll().contains(insertedAchievement))

        assert(groupDao.getAll().first().size == 1)
        assert(checkListItemDao.getAll().first().isEmpty())
        assert(checkListItemDoneDao.getAll().first().isEmpty())
        assert(progressDao.getAll().first().isEmpty())
    }

    private suspend fun getAll(status: Status = Status.ALL, groupIds: List<Long> = emptyList()): List<Achievement> =
        achievementDao.getAll(
            nullableGroupIdsFlag = groupIds.isEmpty(),
            groupIds = groupIds
        )
            .map { it.map { achievementRelations -> achievementRelations.toAchievement() }.filterStatus(status) }
            .first()
}