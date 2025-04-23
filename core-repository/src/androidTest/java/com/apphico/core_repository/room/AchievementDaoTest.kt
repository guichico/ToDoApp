package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Achievement
import com.apphico.core_model.Status
import com.apphico.core_repository.R
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.entities.GroupDB
import com.apphico.core_repository.calendar.room.entities.toAchievement
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AchievementDaoTest {

    private lateinit var appContext: Context
    private lateinit var db: AppDatabase

    private lateinit var groupDao: GroupDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var checkListItemDoneDao: CheckListItemDoneDao
    private lateinit var progressDao: ProgressDao

    private var groupId: Long = 0

    @Before
    fun createDb() {
        appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        groupDao = db.groupDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
        checkListItemDoneDao = db.checkListItemDoneDao()
        progressDao = db.progressDao()

        runBlocking {
            groupId = groupDao.insert(GroupDB(name = appContext.getString(R.string.group_name_2), color = -7745552))
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndReadInList() {
        runBlocking {
            val achievementId = achievementDao.insert(Achievement(name = "Achievement test").toAchievementDB())
            val insertedAchievement = achievementDao.getAchievement(achievementId).toAchievement()

            assert(getAll().contains(insertedAchievement))
        }
    }

    @Test
    @Throws(Exception::class)
    fun testFilters() {
        runBlocking {
            val achievementId = achievementDao.insert(Achievement(name = "Achievement test").toAchievementDB())
            val insertedAchievement = achievementDao.getAchievement(achievementId).toAchievement()


            assert(getAll(Status.DONE).contains(insertedAchievement))
            assert(getAll(Status.UNDONE).contains(insertedAchievement))

            assert(getAll(groupIds = listOf(groupId)).size == 1)
            assert(getAll(groupIds = listOf(Long.MAX_VALUE)).isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        runBlocking {

        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        runBlocking {
            val achievementId = achievementDao.insert(Achievement(name = "Achievement test").toAchievementDB())
            val insertedAchievement = achievementDao.getAchievement(achievementId).toAchievement()

            assert(getAll().contains(insertedAchievement))

            achievementDao.delete(insertedAchievement.toAchievementDB())

            assert(!getAll().contains(insertedAchievement))

            assert(groupDao.getAll().first().size == 1)
            assert(checkListItemDao.getAll().first().isEmpty())
            assert(checkListItemDoneDao.getAll().first().isEmpty())
            assert(progressDao.getAll().first().isEmpty())
        }
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
}