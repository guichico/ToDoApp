package com.apphico.todoapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.apphico.core_repository.calendar.task.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ToDoAppBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            CoroutineScope(Dispatchers.IO).launch {
                taskRepository.setTasksAlarmAfterReboot()
            }
        }
    }
}
