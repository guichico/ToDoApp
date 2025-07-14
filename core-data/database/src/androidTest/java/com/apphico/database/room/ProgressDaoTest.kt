package com.apphico.database.room

import com.apphico.core_model.MeasurementType
import com.apphico.database.room.dao.AchievementDao
import com.apphico.database.room.dao.ProgressDao
import com.apphico.database.room.entities.ProgressDB
import com.apphico.database.room.entities.toAchievement
import com.apphico.database.utils.sampleAchievement
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProgressDaoTest : BaseDaoTest() {
    private lateinit var achievementDao: AchievementDao
    private lateinit var progressDao: ProgressDao

    @Before
    fun init() {
        achievementDao = db.achievementDao()
        progressDao = db.progressDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runTest {
        val achievementId = achievementDao.insert(sampleAchievement(measurementType = MeasurementType.Percentage().intValue))

        progressDao.insertAll(
            listOf(
                ProgressDB(0, achievementId, 0.3f, "Progress 1", getNowDate(), getNowTime()),
                ProgressDB(0, achievementId, 0.6f, "Progress 2", getNowDate(), getNowTime()),
                ProgressDB(0, achievementId, 1f, "Progress 3", getNowDate(), getNowTime())
            )
        )

        assert(progressDao.getAll().first().size == 3)
        assert(achievementDao.getAchievement(achievementId).toAchievement().getProgress() == 1f)

        progressDao.deleteAll(achievementId, emptyList())

        assert(progressDao.getAll().first().isEmpty())
        assert(achievementDao.getAchievement(achievementId).toAchievement().getProgress() == 0f)
    }
}