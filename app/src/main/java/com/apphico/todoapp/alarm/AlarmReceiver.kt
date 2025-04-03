package com.apphico.todoapp.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.extensions.hasNotificationPermission
import com.apphico.todoapp.MainActivity
import com.apphico.todoapp.R
import java.time.LocalTime

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TASK_ID = "task_id"

        const val NOTIFICATION_REQUEST_CODE = 1
        const val STOP_ALARM_ACTION = "stop_alarm"

        const val ALARM_CHANNEL_NAME = "task_alarm_channel"
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

        if (intent.action == STOP_ALARM_ACTION) {
            mediaPlayer.release()
            mediaPlayer.stop()

            val taskId = intent.getLongExtra(TASK_ID, 0L)
            AlarmHelperImpl(context).cancelAlarm(Task(id = taskId))

            NotificationManagerCompat.from(context).cancel(NOTIFICATION_REQUEST_CODE)
        } else {
            mediaPlayer.isLooping = true

            val taskId = intent.getLongExtra(AlarmHelper.TASK_ID, 0L)
            val taskName = intent.getStringExtra(AlarmHelper.TASK_NAME)
            val taskTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(AlarmHelper.TASK_TIME, LocalTime::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra(AlarmHelper.TASK_TIME) as LocalTime
            }

            if (context.hasNotificationPermission() && taskName != null && taskTime != null) {
                context.createNotification(
                    taskId = taskId,
                    taskName = taskName,
                    taskTime = taskTime.toString()
                )
            }

            mediaPlayer.start()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun Context.createNotification(taskId: Long, taskName: String, taskTime: String) {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(ALARM_CHANNEL_NAME, ALARM_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        )

        val openIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_REQUEST_CODE,
            Intent(this, MainActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
            PendingIntent.FLAG_IMMUTABLE
        )

        val stopAlarmIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java).apply {
                action = STOP_ALARM_ACTION
                putExtra(TASK_ID, taskId)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, ALARM_CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(taskName)
            .setContentText(taskTime)
            .setContentIntent(openIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_notification, getString(com.apphico.designsystem.R.string.stop_alarm), stopAlarmIntent)

        NotificationManagerCompat
            .from(this)
            .notify(NOTIFICATION_REQUEST_CODE, notificationBuilder.build())
    }
}