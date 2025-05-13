package com.apphico.todoapp.alarm

import android.app.AlarmManager
import android.content.Context
import com.apphico.core_model.Task
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.extensions.getNowDateTime
import com.apphico.extensions.toMillis
import com.apphico.todoapp.utils.createAlarmIntent
import com.apphico.todoapp.utils.createAlarmIntentWithExtras
import com.apphico.todoapp.utils.hasAlarm
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

class AlarmHelperImpl(
    private val context: Context
) : AlarmHelper {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setAlarm(task: Task) {
        val reminderDateTime = task.reminderDateTime()

        if (reminderDateTime != null && !context.hasAlarm(task.reminderId)) {
            if (task.isRepeatable()) {
                // TODO Fix it
                when (task.daysOfWeek.size) {
                    // DAILY
                    0, 7 -> {
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            reminderDateTime.toMillis(),
                            AlarmManager.INTERVAL_DAY,
                            context.createAlarmIntentWithExtras(task)!!
                        )
                    }

                    // WEEKLY
                    1 -> {
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            reminderDateTime.toMillis(),
                            (AlarmManager.INTERVAL_DAY * 7),
                            context.createAlarmIntentWithExtras(task)!!
                        )
                    }

                    // DOESN'T HAVE A TIME INTERVAL PATTERN SO SET ONE FOR EACH DAY
                    else -> {
                        task.daysOfWeek.forEach { dayOfWeek ->
                            alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                reminderDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek))).toMillis(),
                                (AlarmManager.INTERVAL_DAY * 7),
                                context.createAlarmIntentWithExtras(task)!!
                            )
                        }
                    }
                }
            } else if (reminderDateTime >= getNowDateTime()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderDateTime.toMillis(),
                    context.createAlarmIntentWithExtras(task)!!
                )
            }
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