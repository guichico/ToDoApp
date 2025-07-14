package com.apphico.database.room

import com.apphico.database.room.dao.AchievementDao
import com.apphico.database.room.dao.CheckListItemDao
import com.apphico.database.room.dao.CheckListItemDoneDao
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.entities.CheckListItemDoneDB
import com.apphico.database.room.entities.toCheckListItem
import com.apphico.database.utils.sampleAchievement
import com.apphico.database.utils.sampleAchievementCheckList
import com.apphico.database.utils.sampleTask
import com.apphico.database.utils.sampleTaskCheckList
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CheckListItemDoneDaoTest : BaseDaoTest() {
    private lateinit var taskDao: TaskDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var checkListItemDao: CheckListItemDao
    private lateinit var checkListItemDoneDao: CheckListItemDoneDao

    @Before
    fun init() {
        taskDao = db.taskDao()
        achievementDao = db.achievementDao()
        checkListItemDao = db.checkListItemDao()
        checkListItemDoneDao = db.checkListItemDoneDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runTest {
        val taskId = taskDao.insert(sampleTask())
        val achievementId = achievementDao.insert(sampleAchievement())

        val ids = checkListItemDao.insertAll(
            sampleTaskCheckList(taskId) + sampleAchievementCheckList(achievementId)
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
