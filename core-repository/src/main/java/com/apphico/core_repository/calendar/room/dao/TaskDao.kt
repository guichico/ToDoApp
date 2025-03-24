package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction
import androidx.room.Update
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.TaskWithRelations
import com.apphico.extensions.getInt
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Transaction
    @Query(
        "SELECT taskDB.*, taskDoneDates.hasDone, taskDoneDates.doneDates " +
                "FROM taskDB " +
                "LEFT OUTER JOIN " +
                "( " +
                "SELECT taskDoneId, 1 AS hasDone, group_concat(taskDate) AS doneDates " +
                "FROM TaskDoneDb " +
                "GROUP BY taskDoneId " +
                ") AS taskDoneDates " +
                "ON taskDB.taskId = taskDoneDates.taskDoneId " +
                "WHERE " +
                "  ((((:date BETWEEN startDate AND endDate) AND (daysOfWeek LIKE :dayOfWeek)) " +
                " OR ((:date BETWEEN startDate AND endDate) AND (daysOfWeek LIKE '[]')))" +
                " OR " +
                "    ((:date >= startDate AND endDate IS NULL) AND (daysOfWeek LIKE :dayOfWeek) " +
                " OR ((:date == startDate AND endDate IS NULL) AND (daysOfWeek LIKE '[]'))) " +
                " OR (startDate IS NULL AND :date <= endDate) " +
                " OR (startDate IS NULL AND endDate IS NULL AND daysOfWeek LIKE '[]')) " +
                "AND (:nullableGroupIdsFlag OR taskGroupId IN (:groupIds)) " +
                "AND (:statusAllFlag " +
                " OR (:statusDoneFlag AND (hasDone = 1 AND (doneDates LIKE ('%' || startDate || '%') OR startDate IS NULL))) " +
                " OR (:statusUndoneFlag AND (hasDone IS NULL OR doneDates NOT LIKE ('%' || startDate || '%')))) " +
                "ORDER BY startTime"
    )
    fun getFromDay(
        date: LocalDate,
        dayOfWeek: String = "%${date.dayOfWeek.getInt()}%",
        statusAllFlag: Boolean,
        statusDoneFlag: Boolean,
        statusUndoneFlag: Boolean,
        nullableGroupIdsFlag: Boolean,
        groupIds: List<Long>
    ): Flow<List<TaskWithRelations>>

    @Transaction
    @Query(
        "SELECT taskDB.*, taskDoneDates.hasDone, taskDoneDates.doneDates " +
                "FROM taskDB " +
                "LEFT OUTER JOIN " +
                "( " +
                "SELECT taskDoneId, 1 AS hasDone, group_concat(taskDate) AS doneDates " +
                "FROM TaskDoneDb " +
                "GROUP BY taskDoneId " +
                ") AS taskDoneDates " +
                "ON taskDB.taskId = taskDoneDates.taskDoneId " +
                "WHERE (:fromStartDate <= startDate OR startDate IS NULL OR endDate IS NULL) " +
                "AND (:nullableGroupIdsFlag OR taskGroupId IN (:groupIds)) " +
                "ORDER BY startDate, startTime"
    )
    fun getAllTasks(fromStartDate: LocalDate, nullableGroupIdsFlag: Boolean, groupIds: List<Long>): Flow<List<TaskWithRelations>>

    @Insert
    suspend fun insert(taskDB: TaskDB): Long

    @Update
    suspend fun update(taskDB: TaskDB)

    @Delete
    suspend fun delete(taskDB: TaskDB)
}