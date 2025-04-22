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
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import com.apphico.core_repository.calendar.room.entities.toCheckListItem
import com.apphico.core_repository.calendar.room.entities.toTaskDB
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CheckListItemDoneDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var taskDao: TaskDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var checkListItemDoneDao: CheckListItemDoneDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        taskDao = db.taskDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
        checkListItemDoneDao = db.checkListItemDoneDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() {
        runBlocking {
            val taskId = taskDao.insert(Task(name = "Task test").toTaskDB())
            val achievementId = achievementDao.insert(Achievement(name = "Achievement test").toAchievementDB())

            val ids = checkListItemDao.insertAll(
                listOf(
                    CheckListItemDB(checkListTaskId = taskId, name = "Item 1"),
                    CheckListItemDB(checkListTaskId = taskId, name = "Item 2"),
                    CheckListItemDB(checkListAchievementId = achievementId, name = "Item 1"),
                    CheckListItemDB(checkListAchievementId = achievementId, name = "Item 2"),
                )
            )

            assert(checkListItemDoneDao.getAll().first().isEmpty())

            checkListItemDoneDao.insert(CheckListItemDoneDB(checkListItemDoneId = ids[0], doneDate = getNowDate(), parentDate = null))
            checkListItemDoneDao.insert(CheckListItemDoneDB(checkListItemDoneId = ids[2], doneDate = getNowDate(), parentDate = null))

            assert(checkListItemDoneDao.getAll().first().size == 2)

            assert(taskDao.getTask(taskId).checkList?.map { it.toCheckListItem() }?.filter { it.isDone(null) }?.size == 1)
            assert(achievementDao.getAchievement(achievementId).checkList?.map { it.toCheckListItem() }?.filter { it.isDone(null) }?.size == 1)

            checkListItemDoneDao.delete(ids[0], null)
            checkListItemDoneDao.delete(ids[2], null)

            assert(checkListItemDoneDao.getAll().first().isEmpty())

            assert(taskDao.getTask(taskId).checkList?.map { it.toCheckListItem() }?.filter { it.isDone(null) }.isNullOrEmpty())
            assert(achievementDao.getAchievement(achievementId).checkList?.map { it.toCheckListItem() }?.filter { it.isDone(null) }.isNullOrEmpty())
        }
    }
}
