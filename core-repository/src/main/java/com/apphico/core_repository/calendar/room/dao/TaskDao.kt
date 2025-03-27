package com.apphico.core_repository.calendar.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.core_repository.calendar.room.entities.TaskWithRelations
import com.apphico.extensions.getInt
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao : BaseDao<TaskDB> {

    @Transaction
    @Query(
        "SELECT * FROM TaskComplete " +
                "WHERE (:fromStartDate <= startDate OR startDate IS NULL OR endDate IS NULL) " +
                "AND (:nullableGroupIdsFlag OR taskGroupId IN (:groupIds)) " +
                "ORDER BY startDate, startTime"
    )
    fun getAllTasks(fromStartDate: LocalDate, nullableGroupIdsFlag: Boolean, groupIds: List<Long>): Flow<List<TaskWithRelations>>

    @Transaction
    @Query(
        "SELECT * FROM TaskComplete " +
                "WHERE (hasDeleted IS NULL OR deletedDates NOT LIKE ('%' || :date || '%')) " +
                "AND ((((:date BETWEEN startDate AND endDate) AND (daysOfWeek LIKE :dayOfWeek)) " +
                "   OR ((:date BETWEEN startDate AND endDate) AND (daysOfWeek LIKE '[]')))" +
                "   OR " +
                "      ((:date >= startDate AND endDate IS NULL) AND (daysOfWeek LIKE :dayOfWeek) " +
                "   OR ((:date == startDate AND endDate IS NULL) AND (daysOfWeek LIKE '[]'))) " +
                "   OR (startDate IS NULL AND :date <= endDate) " +
                "   OR (startDate IS NULL AND endDate IS NULL AND daysOfWeek LIKE '[]')) " +
                "  AND (:nullableGroupIdsFlag OR taskGroupId IN (:groupIds)) " +
                "  AND (:statusAllFlag " +
                "   OR (:statusDoneFlag AND (hasDone = 1 AND (doneDates LIKE ('%' || :date || '%') OR startDate IS NULL))) " +
                "   OR (:statusUndoneFlag AND (hasDone IS NULL OR (startDate IS NOT NULL AND doneDates NOT LIKE ('%' || :date || '%'))))) " +
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
    @Query("UPDATE taskdb SET endDate = :endDate WHERE taskId = :taskId")
    suspend fun updateEndDate(taskId: Long, endDate: LocalDate?)

}