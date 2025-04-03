package com.apphico.core_repository.calendar.alarm

import com.apphico.core_model.Task

interface AlarmHelper {

    companion object {
        const val TASK_ID = "task_id"
        const val TASK_NAME = "task_name"
        const val TASK_TIME = "task_time"
    }

    fun setAlarm(task: Task)
    fun cancelAlarm(task: Task)
}