package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AchievementDaoTest {

    private lateinit var appContext: Context
    private lateinit var db: AppDatabase

    private lateinit var achievementDao: AchievementDao

    @Before
    fun createDb() {
        appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        achievementDao = db.achievementDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}