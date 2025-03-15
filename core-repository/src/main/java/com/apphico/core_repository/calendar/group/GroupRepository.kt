package com.apphico.core_repository.calendar.group

import android.util.Log
import com.apphico.core_model.Group
import com.apphico.core_repository.calendar.room.dao.GroupDao
import com.apphico.core_repository.calendar.room.entities.toGroup
import com.apphico.core_repository.calendar.room.entities.toGroupDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>
    suspend fun insertGroup(group: Group): Boolean
    suspend fun updateGroup(group: Group): Boolean
    suspend fun deleteGroup(group: Group): Boolean
}

class GroupRepositoryImpl(
    private val groupDao: GroupDao
) : GroupRepository {

    override fun getGroups(): Flow<List<Group>> =
        groupDao.getAll()
            .map { it.map { it.toGroup() } }

    override suspend fun insertGroup(group: Group): Boolean {
        return try {
            groupDao.insert(group.toGroupDB())

            return true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun updateGroup(group: Group): Boolean {
        return try {
            groupDao.update(group.toGroupDB())

            return true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }

    override suspend fun deleteGroup(group: Group): Boolean {
        return try {
            groupDao.delete(group.toGroupDB())

            return true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            return false
        }
    }
}