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

    override fun setAlarm(task: Task): List<Long> {
        val reminderDateTime = task.reminderDateTime()

        val reminderIds = mutableListOf<Long>()

        if (reminderDateTime != null) {
            if (task.isRepeatable()) {
                when (task.daysOfWeek.size) {
                    // DAILY
                    0, 7 -> {
                        val alarmId = task.key()

                        if (!context.hasAlarm(alarmId)) {
                            alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                reminderDateTime.toMillis(),
                                AlarmManager.INTERVAL_DAY,
                                context.createAlarmIntentWithExtras(alarmId, task)!!
                            )

                            reminderIds.add(alarmId)
                        }
                    }

                    // WEEKLY
                    1 -> {
                        val alarmId = task.key()

                        if (!context.hasAlarm(alarmId)) {
                            alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                reminderDateTime.toMillis(),
                                (AlarmManager.INTERVAL_DAY * 7),
                                context.createAlarmIntentWithExtras(alarmId, task)!!
                            )

                            reminderIds.add(alarmId)
                        }
                    }

                    // DOESN'T HAVE A TIME INTERVAL PATTERN SO SET ONE FOR EACH DAY
                    else -> {
                        task.daysOfWeek.forEach { dayOfWeek ->
                            val alarmId = task.key() + dayOfWeek

                            if (!context.hasAlarm(alarmId)) {
                                alarmManager.setRepeating(
                                    AlarmManager.RTC_WAKEUP,
                                    reminderDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek))).toMillis(),
                                    (AlarmManager.INTERVAL_DAY * 7),
                                    context.createAlarmIntentWithExtras(alarmId, task)!!
                                )

                                reminderIds.add(alarmId)
                            }
                        }
                    }
                }
            } else if (reminderDateTime >= getNowDateTime()) {
                val alarmId = task.key()

                if (!context.hasAlarm(alarmId)) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminderDateTime.toMillis(),
                        context.createAlarmIntentWithExtras(alarmId, task)!!
                    )

                    reminderIds.add(alarmId)
                }
            }
        }

        return reminderIds
    }

    override fun cancelAlarm(reminderId: Long) {
        if (context.hasAlarm(reminderId)) {
            val cancelIntent = context.createAlarmIntent(reminderId)

            alarmManager.cancel(cancelIntent!!)
            cancelIntent.cancel()
        }
    }
}