package com.apphico.core_repository.calendar.achievements

import android.util.Log
import androidx.room.withTransaction
import com.apphico.core_model.Achievement
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.Progress
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.ProgressDao
import com.apphico.core_repository.calendar.room.entities.toAchievement
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import com.apphico.core_repository.calendar.room.entities.toCheckListItemDB
import com.apphico.core_repository.calendar.room.entities.toProgressDB
import com.apphico.extensions.getNowDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AchievementRepository {
    fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>>
    suspend fun setDone(achievement: Achievement): Boolean
    suspend fun insertAchievement(achievement: Achievement): Boolean
    suspend fun updateAchievement(achievement: Achievement): Boolean
    suspend fun deleteAchievement(achievement: Achievement): Boolean
}

class AchievementRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val achievementDao: AchievementDao,
    private val checkListItemDao: CheckListItemDao,
    private val progressDao: ProgressDao
) : AchievementRepository {

    override fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>> =
        achievementDao.getAll(
            statusAllFlag = status == Status.ALL,
            statusDoneFlag = status == Status.DONE,
            statusUndoneFlag = status == Status.UNDONE,
            nullableGroupIdsFlag = groups.isEmpty(),
            groupIds = groups.map { it.id }
        )
            .map { it.map { it.toAchievement() } }
            .map {
                it.filter {
                    when (status) {
                        Status.DONE -> it.getProgress() >= 1f
                        Status.UNDONE -> it.getProgress() < 1f
                        else -> true
                    }
                }
            }

    override suspend fun setDone(achievement: Achievement): Boolean {
        return try {
            achievementDao.update(achievement.copy(doneDate = getNowDate()).toAchievementDB())

            return true
        } catch (ex: Exception) {
            Log.d(AchievementRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun insertAchievement(achievement: Achievement): Boolean {
        return try {
            appDatabase.withTransaction {
                val achievementId = achievementDao.insert(achievement.toAchievementDB())
                var progress = emptyList<Progress>()

                when (achievement.measurementType) {
                    is MeasurementType.TaskDone -> {
                        checkListItemDao.insertAll(achievement.getCheckList().map { it.toCheckListItemDB(achievementId = achievementId) })
                    }

                    is MeasurementType.Percentage -> {
                        progress = achievement.getPercentageProgress().progress
                    }

                    is MeasurementType.Value -> {
                        progress = achievement.getValueProgress().trackedValues
                    }

                    else -> {
                    }
                }

                progressDao.insertAll(progress.map { it.toProgressDB(achievementId) })
            }

            return true
        } catch (ex: Exception) {
            Log.d(AchievementRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateAchievement(achievement: Achievement): Boolean {
        return try {
            appDatabase.withTransaction {
                achievementDao.update(achievement.toAchievementDB())

                var progress = emptyList<Progress>()

                when (achievement.measurementType) {
                    is MeasurementType.TaskDone -> {
                        val checkList = achievement.getCheckList()

                        checkListItemDao.deleteAll(achievementId = achievement.id, checkListItemIds = checkList.map { it.id })
                        checkListItemDao.insertAll(checkList.filter { it.id == 0L }.map { it.toCheckListItemDB(achievementId = achievement.id) })
                        checkListItemDao.updateAll(checkList.filter { it.id != 0L }.map { it.toCheckListItemDB(achievementId = achievement.id) })
                    }

                    is MeasurementType.Percentage -> {
                        progress = achievement.getPercentageProgress().progress
                    }

                    is MeasurementType.Value -> {
                        progress = achievement.getValueProgress().trackedValues
                    }

                    else -> {
                    }
                }

                progressDao.deleteAll(achievement.id, progress.map { it.id })
                progressDao
                    .insertAll(progress.filter { it.id == 0L }.map { it.toProgressDB(achievementId = achievement.id) })
                progressDao
                    .updateAll(progress.filter { it.id != 0L }.map { it.toProgressDB(achievementId = achievement.id) })
            }

            return true
        } catch (ex: Exception) {
            Log.d(AchievementRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun deleteAchievement(achievement: Achievement): Boolean {
        return try {
            achievementDao.delete(achievement.toAchievementDB())

            return true
        } catch (ex: Exception) {
            Log.d(AchievementRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }
}