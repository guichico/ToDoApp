package com.apphico.todoapp.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.apphico.repository.task.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SetAlarmsAfterRebootWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskRepository: TaskRepository
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "SetAlarmsAfterRebootWorker"
    }

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            taskRepository.setTasksAlarmAfterReboot()
        }
        return Result.success()
    }
}