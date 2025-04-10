package com.apphico.core_repository.calendar.room

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apphico.core_model.Group
import com.apphico.core_repository.R
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.dao.TaskDao
import com.apphico.core_repository.calendar.room.entities.AchievementDB
import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.ReminderDB
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.toGroupDB
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Provider

class AppDatabaseInitializer(
    private val appContext: Context,
    private val groupDaoProvider: Provider<GroupDao>,
    private val taskDaoProvider: Provider<TaskDao>,
    private val checkListDaoProvider: Provider<CheckListItemDao>,
    private val achievementDaoProvider: Provider<AchievementDao>
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
        insertAchievements(groupIds)
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
                    startDate = getNowDate(),
                    startTime = getNowTime().withHour(8).withMinute(0),
                    endTime = getNowTime().withHour(8).withMinute(30),
                    endDate = null,
                    reminder = ReminderDB(0, 0, 20),
                    daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7),
                )
            )

            val task2Id = insert(
                TaskDB(
                    name = appContext.getString(R.string.task_name_2),
                    description = appContext.getString(R.string.task_description_2),
                    taskGroupId = groupIds[3],
                    startDate = getNowDate(),
                    startTime = getNowTime().withHour(18).withMinute(30),
                    endDate = getNowDate(),
                    endTime = getNowTime().withHour(19).withMinute(0),
                    reminder = null,
                    daysOfWeek = emptyList()
                )
            )

            val task2CheckList = listOf<CheckListItemDB>(
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_1)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_2)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_3)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_4)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_5)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_6)),
                CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_7))
            )

            checkListDaoProvider.get().insertAll(task2CheckList)
        }
    }

    private suspend fun insertAchievements(groupIds: List<Long>) {
        with(achievementDaoProvider.get()) {
            val achievement1Id = insert(
                AchievementDB(
                    name = appContext.getString(R.string.achievement_name_1),
                    description = appContext.getString(R.string.achievement_description_1),
                    achievementGroupId = groupIds[1],
                    endDate = LocalDate.of(getNowDate().year, 12, 31),
                    doneDate = null
                )
            )

            val achievement1CheckList = listOf<CheckListItemDB>(
                CheckListItemDB(checkListAchievementId = achievement1Id, name = appContext.getString(R.string.achievement_check_list_item_1)),
                CheckListItemDB(checkListAchievementId = achievement1Id, name = appContext.getString(R.string.achievement_check_list_item_2))
            )

            checkListDaoProvider.get().insertAll(achievement1CheckList)

            insert(
                AchievementDB(
                    name = appContext.getString(R.string.achievement_name_2),
                    description = appContext.getString(R.string.achievement_description_2),
                    achievementGroupId = null,
                    endDate = getNowDate().plusMonths(3),
                    doneDate = getNowDate()
                )
            )
        }
    }
}