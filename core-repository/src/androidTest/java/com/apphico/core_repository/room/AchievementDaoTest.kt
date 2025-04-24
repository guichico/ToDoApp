package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Achievement
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.entities.AchievementDB
import com.apphico.core_repository.calendar.room.entities.toAchievement
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import com.apphico.core_repository.utils.sampleGroup
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class AchievementDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var groupDao: GroupDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var checkListItemDoneDao: CheckListItemDoneDao
    private lateinit var progressDao: ProgressDao

    private var groupId: Long = 0

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        groupDao = db.groupDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
        checkListItemDoneDao = db.checkListItemDoneDao()
        progressDao = db.progressDao()

        runBlocking {
            groupId = groupDao.insert(sampleGroup())
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
            statusAllFlag = status == Status.ALL,
            statusDoneFlag = status == Status.DONE,
            statusUndoneFlag = status == Status.UNDONE,
            nullableGroupIdsFlag = groupIds.isEmpty(),
            groupIds = groupIds
        )
            .map { it.map { it.toAchievement() } }
            .first()

    private fun sampleAchievement(
        groupId: Long? = null,
        measurementType: Int = MeasurementType.None.intValue,
        doneDate: LocalDate? = getNowDate().plusMonths(1)
    ) = AchievementDB(
        name = "Achievement test",
        description = "description test",
        achievementGroupId = groupId,
        measurementType = measurementType,
        endDate = getNowDate(),
        doneDate = doneDate,
        valueProgressDB = null
    )
}