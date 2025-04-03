package com.apphico.todoapp.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.extensions.toMillis

class AlarmHelperImpl(
    private val context: Context
) : AlarmHelper {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun Task.createAlarmIntent(extras: Bundle? = null): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            this.id.toInt(),
            Intent(context, AlarmReceiver::class.java).apply { extras?.let { putExtras(it) } },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun Task.createAlarmIntentWithExtras() = createAlarmIntent(
        extras = Bundle()
            .apply {
                putLong(AlarmHelper.TASK_ID, this@createAlarmIntentWithExtras.id)
                putString(AlarmHelper.TASK_NAME, this@createAlarmIntentWithExtras.name)
                putSerializable(AlarmHelper.TASK_TIME, this@createAlarmIntentWithExtras.startTime)
            }
    )

    override fun setAlarm(task: Task) {
        task.reminderDateTime()?.let { alarmSchedule ->
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmSchedule.toMillis(),
                task.createAlarmIntentWithExtras()
            )
        }
    }

    override fun cancelAlarm(task: Task) {
        alarmManager.cancel(task.createAlarmIntent())
    }
}