package com.apphico.core_repository.calendar.achievements

import android.util.Log
import com.apphico.core_model.Achievement
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_repository.calendar.room.dao.AchievementDao
import com.apphico.core_repository.calendar.room.entities.toAchievement
import com.apphico.core_repository.calendar.room.entities.toAchievementDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AchievementRepository {
    fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>>
    suspend fun insertAchievement(achievement: Achievement): Boolean
    suspend fun updateAchievement(achievement: Achievement): Boolean
    suspend fun deleteAchievement(achievement: Achievement): Boolean
}

class AchievementRepositoryImpl(
    private val achievementDao: AchievementDao
) : AchievementRepository {

    override fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>> = achievementDao.getAll().map { it.map { it.toAchievement() } }

    override suspend fun insertAchievement(achievement: Achievement): Boolean {
        return try {
            achievementDao.insert(achievement.toAchievementDB())

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