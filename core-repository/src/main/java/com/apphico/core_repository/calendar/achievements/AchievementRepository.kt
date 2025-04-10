package com.apphico.core_repository.calendar.achievements

import android.util.Log
import androidx.room.withTransaction
import com.apphico.core_model.Achievement
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.dao.CheckListItemDao
import com.apphico.core_repository.calendar.room.dao.PercentageProgressDao
import com.apphico.core_repository.calendar.room.entities.toAchievement
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import com.apphico.core_repository.calendar.room.entities.toCheckListItemDB
import com.apphico.core_repository.calendar.room.entities.toPercentageProgressDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AchievementRepository {
    fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>>
    suspend fun insertAchievement(achievement: Achievement): Boolean
    suspend fun updateAchievement(achievement: Achievement): Boolean
    suspend fun deleteAchievement(achievement: Achievement): Boolean
}

class AchievementRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val achievementDao: AchievementDao,
    private val checkListItemDao: CheckListItemDao,
    private val percentageProgressDao: PercentageProgressDao
) : AchievementRepository {

    override fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>> = achievementDao.getAll().map { it.map { it.toAchievement() } }

    override suspend fun insertAchievement(achievement: Achievement): Boolean {
        return try {
            appDatabase.withTransaction {
                val achievementId = achievementDao.insert(achievement.toAchievementDB())

                Log.d("TEST", "achievement.measurementType: ${achievement.measurementType}")

                when (achievement.measurementType) {
                    is MeasurementType.TaskDone -> {
                        val checkList = (achievement.measurementType as MeasurementType.TaskDone).checkList
                        checkListItemDao.insertAll(checkList.map { it.toCheckListItemDB(achievementId = achievementId) })
                    }

                    is MeasurementType.Percentage -> {
                        val percentageProgress = (achievement.measurementType as MeasurementType.Percentage).percentageProgress
                        percentageProgressDao.insertAll(percentageProgress.map { it.toPercentageProgressDB(achievementId) })
                    }

                    else -> {

                    }
                }
            }

            return true
        } catch (ex: Exception) {
            Log.d(AchievementRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateAchievement(achievement: Achievement): Boolean {
        return try {
            achievementDao.update(achievement.toAchievementDB())

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