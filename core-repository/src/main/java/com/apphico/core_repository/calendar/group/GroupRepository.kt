package com.apphico.core_repository.calendar.group

import com.apphico.core_model.Group
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.toGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>
}

class GroupRepositoryImpl(
    val appDatabase: AppDatabase
) : GroupRepository {

    override fun getGroups(): Flow<List<Group>> =
        flow {
            emit(
                appDatabase
                    .groupDao()
                    .getAll()
                    .map { it.toGroup() }
            )
        }
}