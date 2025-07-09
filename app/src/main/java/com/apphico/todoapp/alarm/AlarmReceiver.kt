package com.apphico.todoapp.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.core_repository.calendar.task.TaskRepository
import com.apphico.extensions.hasNotificationPermission
import com.apphico.todoapp.R
import com.apphico.todoapp.utils.createActionStopAlarmIntent
import com.apphico.todoapp.utils.createOpenTaskIntent
import com.apphico.todoapp.utils.getAlarmId
import com.apphico.todoapp.utils.getTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    @Inject
    lateinit var taskRepository: TaskRepository

    companion object {
        const val ALARM_CHANNEL_ID = "task_alarm_channel"

        const val STOP_ALARM_REQUEST_CODE = 1
        const val OPEN_TASK_REQUEST_CODE = 2
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmHelper.ALARM_ACTION -> {
                if (context.hasNotificationPermission()) {
                    val task = intent.getTask()

                    @SuppressLint("MissingPermission")
                    context.createNotification(task)

                    if (task.reminder?.soundAlarm == true) mediaPlayerHelper.start()

                    CoroutineScope(Dispatchers.IO).launch {
                        taskRepository.updateTaskNextAlarm(task.id)
                    }
                }
            }

            AlarmHelper.STOP_ALARM_ACTION -> {
                val alarmId = intent.getAlarmId()

                alarmHelper.cancelAlarm(alarmId)

                mediaPlayerHelper.stop()

                NotificationManagerCompat.from(context).cancel(alarmId.toInt())
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun Context.createNotification(task: Task) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(ALARM_CHANNEL_ID, ALARM_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH)
        )

        val notificationBuilder = NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(task.name)
            .setContentText(task.startTime.toString())
            .setContentIntent(createOpenTaskIntent(task))

        if (task.reminder?.soundAlarm == true) {
            notificationBuilder.addAction(
                R.drawable.ic_notification,
                getString(com.apphico.designsystem.R.string.stop_alarm),
                createActionStopAlarmIntent(task.reminderId)
            )
        } else {
            notificationBuilder.setAutoCancel(true)
        }

        NotificationManagerCompat
            .from(this)
            .notify(task.reminderId.toInt(), notificationBuilder.build())
    }
}
