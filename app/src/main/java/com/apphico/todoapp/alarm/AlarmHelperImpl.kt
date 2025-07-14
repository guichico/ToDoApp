package com.apphico.todoapp.alarm

import android.app.AlarmManager
import android.content.Context
import androidx.core.content.getSystemService
import com.apphico.core_model.Task
import com.apphico.extensions.toMillis
import com.apphico.repository.alarm.AlarmHelper
import com.apphico.todoapp.utils.createAlarmIntent
import com.apphico.todoapp.utils.createAlarmIntentWithExtras
import com.apphico.todoapp.utils.hasAlarm
import java.time.LocalDateTime

class AlarmHelperImpl(
    private val context: Context
) : AlarmHelper {

    private val alarmManager by lazy { context.getSystemService<AlarmManager>() }

    override fun setAlarm(alarmId: Long, dateTime: LocalDateTime, task: Task) {
        if (!context.hasAlarm(alarmId)) {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateTime.toMillis(),
                context.createAlarmIntentWithExtras(alarmId, task)!!
            )
        }
    }

    override fun cancelAlarm(reminderId: Long) {
        if (context.hasAlarm(reminderId)) {
            val cancelIntent = context.createAlarmIntent(reminderId)

            alarmManager?.cancel(cancelIntent!!)
            cancelIntent?.cancel()
        }
    }
}
