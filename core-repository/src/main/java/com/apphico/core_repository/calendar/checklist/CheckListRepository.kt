package com.apphico.core_repository.calendar.checklist

import android.util.Log
import com.apphico.core_model.CheckListItem
import com.apphico.core_repository.calendar.room.dao.CheckListItemDoneDao
import com.apphico.core_repository.calendar.room.entities.CheckListItemDoneDB
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.extensions.getNowDate
import java.time.LocalDate

interface CheckListRepository {
    suspend fun changeCheckListItemDone(checkListItem: CheckListItem, parentDate: LocalDate?, isDone: Boolean): Boolean
}

class CheckListRepositoryImpl(
    private val checkListItemDoneDao: CheckListItemDoneDao
) : CheckListRepository {

    override suspend fun changeCheckListItemDone(checkListItem: CheckListItem, parentDate: LocalDate?, isDone: Boolean): Boolean =
        try {
            if (isDone) {
                checkListItemDoneDao.insert(
                    CheckListItemDoneDB(checkListItemDoneId = checkListItem.id, doneDate = getNowDate(), parentDate = parentDate)
                )
            } else {
                checkListItemDoneDao.delete(checkListItem.id, parentDate)
            }

            true
        } catch (ex: Exception) {
            Log.d(TaskRepository::class.simpleName, ex.stackTrace.toString())
            false
        }
}