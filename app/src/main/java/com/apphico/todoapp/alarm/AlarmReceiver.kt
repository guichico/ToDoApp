package com.apphico.todoapp.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.extensions.hasNotificationPermission
import com.apphico.todoapp.MainActivity
import com.apphico.todoapp.R
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    companion object {
        const val ALARM_CHANNEL_ID = "task_alarm_channel"

        const val STOP_ALARM_REQUEST_CODE = 1
        const val OPEN_TASK_REQUEST_CODE = 2
    }

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(AlarmHelper.TASK_ID, 0L)

        when (intent.action) {
            AlarmHelper.ALARM_ACTION -> {
                val taskName = intent.getStringExtra(AlarmHelper.TASK_NAME)
                val taskTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra(AlarmHelper.TASK_TIME, LocalTime::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getSerializableExtra(AlarmHelper.TASK_TIME) as LocalTime
                }

                if (context.hasNotificationPermission() && taskName != null && taskTime != null) {
                    @SuppressLint("MissingPermission")
                    context.createNotification(
                        taskId = taskId,
                        taskName = taskName,
                        taskTime = taskTime.toString()
                    )
                }

                mediaPlayerHelper.start()
            }

            AlarmHelper.STOP_ALARM_ACTION -> {
                alarmHelper.cancelAlarm(taskId)

                mediaPlayerHelper.stop()

                NotificationManagerCompat.from(context).cancel(taskId.toInt())
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun Context.createNotification(taskId: Long, taskName: String, taskTime: String) {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(ALARM_CHANNEL_ID, ALARM_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH)
        )

        val openTaskIntent = PendingIntent.getActivity(
            this,
            OPEN_TASK_REQUEST_CODE,
            Intent(
                Intent.ACTION_VIEW,
                AlarmHelper.ALARM_NOTIFICATION_DEEPLINK.toUri(),
                this,
                MainActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = AlarmHelper.OPEN_TASK_ACTION
                putExtra(AlarmHelper.TASK_ID, taskId)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopAlarmIntent = PendingIntent.getBroadcast(
            this,
            STOP_ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java).apply {
                action = AlarmHelper.STOP_ALARM_ACTION
                putExtra(AlarmHelper.TASK_ID, taskId)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(taskName)
            .setContentText(taskTime)
            .setContentIntent(openTaskIntent)
            .addAction(R.drawable.ic_notification, getString(com.apphico.designsystem.R.string.stop_alarm), stopAlarmIntent)

        NotificationManagerCompat
            .from(this)
            .notify(taskId.toInt(), notificationBuilder.build())
    }
}