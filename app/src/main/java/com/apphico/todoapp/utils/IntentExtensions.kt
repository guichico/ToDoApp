package com.apphico.todoapp.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.net.toUri
import com.apphico.core_model.Reminder
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.todoapp.MainActivity
import com.apphico.todoapp.alarm.AlarmReceiver
import com.apphico.todoapp.alarm.AlarmReceiver.Companion.OPEN_TASK_REQUEST_CODE
import com.apphico.todoapp.alarm.AlarmReceiver.Companion.STOP_ALARM_REQUEST_CODE
import java.time.LocalDate
import java.time.LocalTime

fun Intent.getAlarmId() = this.getLongExtra(AlarmHelper.ALARM_ID, 0L)

fun Intent.getTask() =
    Task(
        id = this.getLongExtra(AlarmHelper.TASK_ID, 0L),
        name = this.getStringExtra(AlarmHelper.TASK_NAME) ?: "",
        startDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(AlarmHelper.TASK_DATE, LocalDate::class.java)
        } else {
            @Suppress("DEPRECATION")
            this.getSerializableExtra(AlarmHelper.TASK_DATE) as LocalDate
        },
        startTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(AlarmHelper.TASK_TIME, LocalTime::class.java)
        } else {
            @Suppress("DEPRECATION")
            this.getSerializableExtra(AlarmHelper.TASK_TIME) as LocalTime
        },
        reminder = Reminder(soundAlarm = this.getBooleanExtra(AlarmHelper.SOUND_ALARM, false))
    )

fun Context.hasAlarm(reminderId: Long) =
    createAlarmIntent(reminderId, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE) != null

fun Context.createAlarmIntent(
    alarmId: Long,
    flags: Int = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    extras: Bundle? = null
): PendingIntent? =
    PendingIntent.getBroadcast(
        this,
        alarmId.toInt(),
        Intent(this, AlarmReceiver::class.java).apply {
            action = AlarmHelper.ALARM_ACTION
            putExtra(AlarmHelper.ALARM_ID, alarmId)
            extras?.let { putExtras(it) }
        },
        flags
    )

fun Context.createAlarmIntentWithExtras(alarmId: Long, task: Task) =
    createAlarmIntent(
        alarmId = alarmId,
        flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        extras = task.getTaskBundle()
    )

fun Context.createActionStopAlarmIntent(alarmId: Long): PendingIntent =
    PendingIntent.getBroadcast(
        this,
        STOP_ALARM_REQUEST_CODE,
        Intent(this, AlarmReceiver::class.java).apply {
            action = AlarmHelper.STOP_ALARM_ACTION
            putExtra(AlarmHelper.ALARM_ID, alarmId)
        },
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

fun Context.createOpenTaskIntent(task: Task): PendingIntent =
    PendingIntent.getActivity(
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
            putExtras(task.getTaskBundle())
        },
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

private fun Task.getTaskBundle() = Bundle().apply {
    putLong(AlarmHelper.TASK_ID, this@getTaskBundle.id)
    putString(AlarmHelper.TASK_NAME, this@getTaskBundle.name)
    putSerializable(AlarmHelper.TASK_DATE, this@getTaskBundle.startDate)
    putSerializable(AlarmHelper.TASK_TIME, this@getTaskBundle.startTime)
    putBoolean(AlarmHelper.SOUND_ALARM, this@getTaskBundle.reminder?.soundAlarm == true)
}