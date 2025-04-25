package com.apphico.core_repository.utils

import com.apphico.core_repository.calendar.room.entities.CheckListItemDB
import com.apphico.core_repository.calendar.room.entities.GroupDB
import com.apphico.core_repository.calendar.room.entities.LocationDB
import com.apphico.core_repository.calendar.room.entities.ReminderDB
import com.apphico.core_repository.calendar.room.entities.TaskDB
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime
import java.time.LocalDate

fun sampleGroup() = GroupDB(name = "Group test", color = -7745552)

fun sampleTask(groupId: Long? = null, endDate: LocalDate? = null) = TaskDB(
    name = "Task test",
    description = "description test",
    taskGroupId = groupId,
    startDate = getNowDate(),
    startTime = getNowTime().withHour(8).withMinute(0),
    endTime = getNowTime().withHour(8).withMinute(30),
    endDate = endDate,
    reminder = ReminderDB(0, 0, 20),
    daysOfWeek = listOf(2, 4, 6)
)

fun sampleTaskCheckList(taskId: Long) = listOf(
    CheckListItemDB(checkListTaskId = taskId, name = "Item 1"),
    CheckListItemDB(checkListTaskId = taskId, name = "Item 2")
)

fun sampleAchievementCheckList(achievementId: Long) = listOf(
    CheckListItemDB(checkListAchievementId = achievementId, name = "Item 1"),
    CheckListItemDB(checkListAchievementId = achievementId, name = "Item 2")
)

fun sampleLocation(taskId: Long) = LocationDB(
    locationTaskId = taskId,
    latitude = 37.42253323528007,
    longitude = -122.08524665141145,
    address = "1600 Amphitheater Pkwy, Mountain View, CA 94043, United States"
)