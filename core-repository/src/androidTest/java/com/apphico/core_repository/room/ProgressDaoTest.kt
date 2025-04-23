package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_model.Achievement
import com.apphico.core_model.MeasurementType
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.entities.ProgressDB
import com.apphico.core_repository.calendar.room.entities.toAchievement
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProgressDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var achievementDao: AchievementDao
    private lateinit var progressDao: ProgressDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        achievementDao = db.achievementDao()
        progressDao = db.progressDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runTest {
        val achievementId =
            achievementDao.insert(Achievement(name = "Achievement test", measurementType = MeasurementType.Percentage()).toAchievementDB())

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