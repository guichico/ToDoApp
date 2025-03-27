package com.apphico.core_repository.calendar.achievements

import com.apphico.core_model.Achievement
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.fakeData.mockedAchievements
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AchievementsRepository {
    fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>>
}

class AchievementsRepositoryImpl : AchievementsRepository {
    override fun getAll(status: Status, groups: List<Group>): Flow<List<Achievement>> = flow {
        emit(mockedAchievements)
    }
}