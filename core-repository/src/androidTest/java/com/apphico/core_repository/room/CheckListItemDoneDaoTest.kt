package com.apphico.core_repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CheckListItemDoneDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var checkListItemDoneDao: CheckListItemDoneDao

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()

        checkListItemDoneDao = db.checkListItemDoneDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}