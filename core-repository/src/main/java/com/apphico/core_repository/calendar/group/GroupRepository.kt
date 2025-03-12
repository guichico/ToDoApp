package com.apphico.core_repository.calendar.group

import android.util.Log
import com.apphico.core_model.Group
import com.apphico.core_repository.calendar.room.AppDatabase
import com.apphico.core_repository.calendar.room.toGroup
import com.apphico.core_repository.calendar.room.toGroupDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>
    suspend fun insertGroup(group: Group): Boolean
    suspend fun updateGroup(group: Group): Boolean
    suspend fun deleteGroup(group: Group): Boolean
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

    override suspend fun insertGroup(group: Group): Boolean {
        return try {
            appDatabase.groupDao().insert(group.toGroupDB())

            return true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateGroup(group: Group): Boolean {
        return try {
            appDatabase.groupDao().update(group.toGroupDB())

            return true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun deleteGroup(group: Group): Boolean {
        return try {
            appDatabase.groupDao().delete(group.toGroupDB())

            return true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }
}