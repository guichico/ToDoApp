package com.apphico.core_repository.calendar.room

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apphico.core_model.Group
import com.apphico.core_repository.R
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.toGroupDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Provider

class AppDatabaseInitializer(
    private val appContext: Context,
    private val groupDaoProvider: Provider<GroupDao>,
    private val taskDaoProvider: Provider<TaskDao>,
    private val checkListDaoProvider: Provider<CheckListItemDao>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        applicationScope.launch(Dispatchers.IO) {
            insertSampleData()
        }
    }

    private suspend fun insertSampleData() {
        val groupIds = insertGroups()
        insertTasks(groupIds)
    }

    private suspend fun insertGroups(): List<Long> {
        val groups = listOf(
            Group(name = appContext.getString(R.string.group_name_1), color = -8432327), // Work
            Group(name = appContext.getString(R.string.group_name_2), color = -7745552), // Health
            Group(name = appContext.getString(R.string.group_name_3), color = -14198462), // Family
            Group(name = appContext.getString(R.string.group_name_4), color = -402340), // Home
            Group(name = appContext.getString(R.string.group_name_5), color = -1886900), // Studies
            Group(name = appContext.getString(R.string.group_name_6), color = -17587), // Entertainment
            Group(name = appContext.getString(R.string.group_name_7), color = -10938214), // Productivity
        )

        return groupDaoProvider.get().insertAll(groups.map { it.toGroupDB() })
    }

    private suspend fun insertTasks(groupIds: List<Long>) {
        with(taskDaoProvider.get()) {
            insert(
                TaskDB(
                    name = appContext.getString(R.string.task_name_1),
                    description = appContext.getString(R.string.task_description_1),
                    taskGroupId = groupIds[1],
                    startDate = LocalDate.now(),
                    startTime = LocalTime.now().withHour(8).withMinute(0),
                    endTime = LocalTime.now().withHour(8).withMinute(30),
                    endDate = null,
                    reminder = LocalTime.of(7, 40),
                    daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7),
                )
            )

            val task2Id = insert(
                TaskDB(
                    name = appContext.getString(R.string.task_name_2),
                    description = appContext.getString(R.string.task_description_2),
                    taskGroupId = groupIds[3],
                    startDate = LocalDate.now(),
                    startTime = LocalTime.now().withHour(18).withMinute(30),
                    endDate = LocalDate.now(),
                    endTime = LocalTime.now().withHour(19).withMinute(0),
                    reminder = null,
                    daysOfWeek = emptyList()
                )
            )

            val task2CheckList = listOf<CheckListItemDB>(
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_1)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_2)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_3)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_4)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_5)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_6)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.check_list_item_7))
            )

            checkListDaoProvider.get().insertAll(task2CheckList)
        }
    }
}