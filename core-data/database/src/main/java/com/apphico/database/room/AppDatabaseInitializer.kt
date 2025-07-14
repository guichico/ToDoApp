package com.apphico.database.room

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementValueUnit
import com.apphico.database.R
import com.apphico.database.room.dao.AchievementDao
import com.apphico.database.room.dao.CheckListItemDao
import com.apphico.database.room.dao.GroupDao
import com.apphico.database.room.dao.ProgressDao
import com.apphico.database.room.dao.TaskDao
import com.apphico.database.room.entities.AchievementDB
import com.apphico.database.room.entities.CheckListItemDB
import com.apphico.database.room.entities.GroupDB
import com.apphico.database.room.entities.ProgressDB
import com.apphico.database.room.entities.ReminderDB
import com.apphico.database.room.entities.TaskDB
import com.apphico.database.room.entities.ValueProgressDB
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
    private val achievementDaoProvider: Provider<AchievementDao>,
    private val progressDaoProvider: Provider<ProgressDao>
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
            GroupDB(name = appContext.getString(R.string.group_name_1), color = -8432327), // Work
            GroupDB(name = appContext.getString(R.string.group_name_2), color = -7745552), // Health
            GroupDB(name = appContext.getString(R.string.group_name_3), color = -14198462), // Family
            GroupDB(name = appContext.getString(R.string.group_name_4), color = -1886900), // Finances
            GroupDB(name = appContext.getString(R.string.group_name_5), color = -402340), // Home
            GroupDB(name = appContext.getString(R.string.group_name_6), color = -17587), // Studies
            GroupDB(name = appContext.getString(R.string.group_name_7), color = -13095), // Entertainment
            GroupDB(name = appContext.getString(R.string.group_name_8), color = -10938214), // Productivity
        )

        return groupDaoProvider.get().insertAll(groups)
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
                    taskGroupId = groupIds[4],
                    startDate = getNowDate(),
                    startTime = getNowTime().withHour(18).withMinute(30),
                    endDate = getNowDate(),
                    endTime = getNowTime().withHour(19).withMinute(0),
                    reminder = null,
                    daysOfWeek = emptyList()
                )
            )

            checkListDaoProvider.get().insertAll(
                listOf(
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_1)),
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_2)),
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_3)),
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_4)),
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_5)),
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_6)),
                    CheckListItemDB(checkListTaskId = task2Id, name = appContext.getString(R.string.task_check_list_item_7))
                )
            )
        }
    }

    private suspend fun insertAchievements(groupIds: List<Long>) {
        with(achievementDaoProvider.get()) {
            val achievement1Id = insert(
                AchievementDB(
                    name = appContext.getString(R.string.achievement_name_1),
                    description = appContext.getString(R.string.achievement_description_1),
                    achievementGroupId = groupIds[1],
                    measurementType = MeasurementType.TaskDone().intValue,
                    endDate = LocalDate.of(getNowDate().year, 12, 31),
                    doneDate = null,
                    valueProgressDB = null
                )
            )

            checkListDaoProvider.get().insertAll(
                listOf(
                    CheckListItemDB(checkListAchievementId = achievement1Id, name = appContext.getString(R.string.achievement_check_list_item_1)),
                    CheckListItemDB(checkListAchievementId = achievement1Id, name = appContext.getString(R.string.achievement_check_list_item_2))
                )
            )

            val achievement2Id = insert(
                AchievementDB(
                    name = appContext.getString(R.string.achievement_name_2),
                    description = appContext.getString(R.string.achievement_description_2),
                    achievementGroupId = groupIds[3],
                    measurementType = MeasurementType.Value().intValue,
                    endDate = getNowDate().plusMonths(12),
                    doneDate = null,
                    valueProgressDB = ValueProgressDB(
                        unit = MeasurementValueUnit.CURRENCY.value,
                        startingValue = 0f,
                        goalValue = 12000f
                    )
                )
            )

            progressDaoProvider.get().insertAll(
                listOf(
                    ProgressDB(
                        achievementProgressId = achievement2Id,
                        progress = 1000f,
                        description = null,
                        date = getNowDate().plusMonths(1),
                        time = null
                    ),
                    ProgressDB(
                        achievementProgressId = achievement2Id,
                        progress = 2000f,
                        description = null,
                        date = getNowDate().plusMonths(2),
                        time = null
                    ),
                    ProgressDB(
                        achievementProgressId = achievement2Id,
                        progress = 3000f,
                        description = null,
                        date = getNowDate().plusMonths(3),
                        time = null
                    )
                )
            )
        }
    }
}