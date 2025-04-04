package com.apphico.todoapp.alarm

import android.app.AlarmManager
import android.content.Context
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.extensions.toMillis
import com.apphico.todoapp.utils.createAlarmIntent
import com.apphico.todoapp.utils.createAlarmIntentWithExtras
import com.apphico.todoapp.utils.hasAlarm

class AlarmHelperImpl(
    private val context: Context
) : AlarmHelper {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setAlarm(task: Task) {
        val reminderDateTime = task.reminderDateTime()

        if (reminderDateTime != null && !context.hasAlarm(task.reminderId)) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminderDateTime.toMillis(),
                context.createAlarmIntentWithExtras(task)!!
            )
        }
    }

    override fun cancelAlarm(reminderId: Long) {
        if (context.hasAlarm(reminderId)) {
            val cancelIntent = context.createAlarmIntent(reminderId)

            alarmManager.cancel(cancelIntent!!)
            cancelIntent.cancel()
        }
    }
}