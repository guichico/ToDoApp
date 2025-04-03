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

    private fun createAlarmIntent(taskId: Long, extras: Bundle? = null): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmHelper.ALARM_ACTION
                putExtra(AlarmHelper.TASK_ID, taskId)
                extras?.let { putExtras(it) }
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun createAlarmIntentWithExtras(task: Task) = createAlarmIntent(
        taskId = task.id,
        extras = Bundle()
            .apply {
                putString(AlarmHelper.TASK_NAME, task.name)
                putSerializable(AlarmHelper.TASK_TIME, task.startTime)
            }
    )

    override fun setAlarm(task: Task) {
        task.reminderDateTime()?.let { alarmSchedule ->
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmSchedule.toMillis(),
                createAlarmIntentWithExtras(task)
            )
        }
    }

    override fun cancelAlarm(taskId: Long) {
        alarmManager.cancel(createAlarmIntent(taskId))
    }
}