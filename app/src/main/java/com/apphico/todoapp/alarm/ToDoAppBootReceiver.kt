package com.apphico.todoapp.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDoAppBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                val setAlarmsAfterReboot = OneTimeWorkRequestBuilder<SetAlarmsAfterRebootWorker>()
                    .addTag(SetAlarmsAfterRebootWorker.TAG)
                    .build()

                WorkManager.getInstance(context)
                    .enqueueUniqueWork(
                        uniqueWorkName = SetAlarmsAfterRebootWorker.TAG,
                        existingWorkPolicy = ExistingWorkPolicy.REPLACE,
                        request = setAlarmsAfterReboot
                    )
            }
        }
    }
}
