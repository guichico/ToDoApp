package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Achievement
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CheckListItemDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var taskDao: TaskDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        taskDao = db.taskDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runTest {
        val taskId = taskDao.insert(Task(name = "Task test").toTaskDB())
        val achievementId = achievementDao.insert(Achievement(name = "Achievement test").toAchievementDB())

        checkListItemDao.insertAll(
            listOf(
                CheckListItemDB(checkListTaskId = taskId, name = "Item 1"),
                CheckListItemDB(checkListTaskId = taskId, name = "Item 2"),
                CheckListItemDB(checkListAchievementId = achievementId, name = "Item 1"),
                CheckListItemDB(checkListAchievementId = achievementId, name = "Item 2"),
            )
        )

        assert(checkListItemDao.getAll().first().size == 4)
        assert(taskDao.getTask(taskId).checkList?.size == 2)
        assert(achievementDao.getAchievement(achievementId).checkList?.size == 2)

        checkListItemDao.deleteAll(taskId = taskId, checkListItemIds = emptyList())

        assert(checkListItemDao.getAll().first().size == 2)
        assert(taskDao.getTask(taskId).checkList.isNullOrEmpty())
        assert(achievementDao.getAchievement(achievementId).checkList?.size == 2)

        checkListItemDao.deleteAll(achievementId = achievementId, checkListItemIds = emptyList())

        assert(checkListItemDao.getAll().first().isEmpty())
        assert(taskDao.getTask(taskId).checkList.isNullOrEmpty())
        assert(achievementDao.getAchievement(achievementId).checkList.isNullOrEmpty())
    }
}