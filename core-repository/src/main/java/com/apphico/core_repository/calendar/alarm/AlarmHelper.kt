package com.apphico.core_repository.calendar.alarm

import com.apphico.core_model.Task

interface AlarmHelper {

    companion object {
        const val ALARM_NOTIFICATION_DEEPLINK = "taskhico://alarm"

        const val ALARM_ACTION = "alarm"
        const val STOP_ALARM_ACTION = "stop_alarm"
        const val OPEN_TASK_ACTION = "open_task"

        const val ALARM_ID = "alarm_id"
        const val TASK_ID = "task_id"
        const val TASK_NAME = "task_name"
        const val TASK_TIME = "task_time"
        const val SOUND_ALARM = "sound_alarm"
    }

    fun setAlarm(task: Task): List<Long>
    fun cancelAlarm(taskKey: Long)
}