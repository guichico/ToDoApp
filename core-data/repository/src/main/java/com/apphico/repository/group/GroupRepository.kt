package com.apphico.repository.group

import android.util.Log
import com.apphico.core_model.Group
import com.apphico.database.room.dao.GroupDao
import com.apphico.database.room.entities.toGroup
import com.apphico.database.room.entities.toGroupDB
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
            .map { it.map { groupDB -> groupDB.toGroup() } }

    override suspend fun insertGroup(group: Group): Boolean =
        try {
            groupDao.insert(group.toGroupDB())

            true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            false
        }

    override suspend fun updateGroup(group: Group): Boolean =
        try {
            groupDao.update(group.toGroupDB())

            true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            false
        }

    override suspend fun deleteGroup(group: Group): Boolean =
        try {
            groupDao.delete(group.toGroupDB())

            true
        } catch (ex: Exception) {
            Log.d(GroupRepository::class.simpleName, ex.stackTrace.toString())
            false
        }
}