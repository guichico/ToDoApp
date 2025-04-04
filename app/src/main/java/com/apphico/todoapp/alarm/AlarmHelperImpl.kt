package com.apphico.todoapp.alarm

import android.app.AlarmManager
import android.content.Context
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.extensions.toMillis
import com.apphico.todoapp.utils.createAlarmIntent
import com.apphico.todoapp.utils.createAlarmIntentWithExtras

class AlarmHelperImpl(
    private val context: Context
) : AlarmHelper {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setAlarm(task: Task) {
        task.reminderDateTime()?.let { alarmSchedule ->
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmSchedule.toMillis(),
                context.createAlarmIntentWithExtras(task)
            )
        }
    }

    override fun cancelAlarm(taskKey: Long) {
        alarmManager.cancel(context.createAlarmIntent(taskKey))
    }
}