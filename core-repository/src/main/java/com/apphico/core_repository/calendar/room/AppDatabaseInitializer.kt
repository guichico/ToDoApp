package com.apphico.core_repository.calendar.room

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.GroupDB
import com.apphico.core_repository.calendar.room.entities.TaskDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Provider

class AppDatabaseInitializer(
    private val groupProvider: Provider<GroupDao>,
    private val taskProvider: Provider<TaskDao>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        applicationScope.launch(Dispatchers.IO) {
            insertSampleData()
        }
    }

    suspend fun insertSampleData() {
        val groups = listOf(
            GroupDB(
                groupId = 0,
                name = "Saúde",
                color = -7745552
            ),
        )

        val groupIds = groupProvider.get().insert(groups)

        val task = TaskDB(
            taskId = 0,
            taskGroupId = groupIds[0],
            name = "Correr (tarefa exemplo)",
            isDone = false,
            description = "Correr 5km todas as manhãs",
            startDate = LocalDateTime.now().withHour(8).withMinute(0),
            endDate = LocalDateTime.now().withHour(8).withMinute(30),
            reminder = LocalTime.of(7, 50),
            daysOfWeek = listOf(1, 2, 3, 4, 5),
        )

        taskProvider.get().insert(task)
    }

    suspend fun insertGroups() {

    }

    suspend fun insertTask() {

    }
}