package com.apphico.core_repository.room

import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.utils.sampleAchievement
import com.apphico.core_repository.utils.sampleAchievementCheckList
import com.apphico.core_repository.utils.sampleTask
import com.apphico.core_repository.utils.sampleTaskCheckList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CheckListItemDaoTest : BaseDaoTest() {
    private lateinit var taskDao: TaskDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao

    @Before
    fun init() {
        taskDao = db.taskDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runTest {
        val taskId = taskDao.insert(sampleTask())
        val achievementId = achievementDao.insert(sampleAchievement())

        checkListItemDao.insertAll(
            sampleTaskCheckList(taskId) + sampleAchievementCheckList(achievementId)
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