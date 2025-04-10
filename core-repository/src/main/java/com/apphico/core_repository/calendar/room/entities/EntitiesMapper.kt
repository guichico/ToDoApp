package com.apphico.core_repository.calendar.room.entities

import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Coordinates
import com.apphico.core_model.Group
import com.apphico.core_model.Location
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.Reminder
import com.apphico.core_model.Task

fun Group.toGroupDB(): GroupDB =
    GroupDB(
        groupId = this.id,
        name = this.name,
        color = this.color
    )

fun GroupDB.toGroup(): Group =
    Group(
        id = this.groupId,
        name = this.name,
        color = this.color
    )

fun CheckListItem.toCheckListItemDB(taskId: Long? = null, achievementId: Long? = null): CheckListItemDB =
    CheckListItemDB(
        checkListItemId = this.id,
        checkListTaskId = taskId,
        checkListAchievementId = achievementId,
        name = this.name
    )

fun CheckListWithDone.toCheckListItem(): CheckListItem =
    CheckListItem(
        id = this.checkListItem.checkListItemId,
        name = this.checkListItem.name,
        hasDone = this.checkListItemHasDone,
        doneDates = this.checkListItemDoneDates
    )

fun Location.toLocationDB(taskId: Long): LocationDB =
    LocationDB(
        locationId = this.id,
        locationTaskId = taskId,
        latitude = this.coordinates.first,
        longitude = this.coordinates.second,
        address = this.address
    )

fun LocationDB.toLocation(): Location =
    Location(
        id = this.locationId,
        coordinates = Coordinates(this.latitude, this.longitude),
        address = this.address
    )

fun Reminder.toReminderDB(): ReminderDB =
    ReminderDB(
        days = this.days,
        hours = this.hours,
        minutes = this.minutes,
        soundAlarm = this.soundAlarm
    )

fun ReminderDB.toReminder(): Reminder =
    Reminder(
        days = this.days,
        hours = this.hours,
        minutes = this.minutes,
        soundAlarm = this.soundAlarm
    )

fun Task.toTaskDB(): TaskDB =
    TaskDB(
        taskId = this.id,
        name = this.name,
        description = this.description,
        taskGroupId = this.group?.id,
        startDate = this.startDate,
        startTime = this.startTime,
        endDate = this.endDate,
        endTime = this.endTime,
        daysOfWeek = this.daysOfWeek,
        reminderId = this.reminderId,
        reminder = this.reminder?.toReminderDB()
    )

fun TaskWithRelations.toTask(): Task =
    Task(
        id = this.taskComplete.taskDB.taskId,
        name = this.taskComplete.taskDB.name,
        description = this.taskComplete.taskDB.description,
        group = this.groupDB?.toGroup(),
        checkList = this.checkList?.map { it.toCheckListItem() } ?: emptyList(),
        startDate = this.taskComplete.taskDB.startDate,
        startTime = this.taskComplete.taskDB.startTime,
        endDate = this.taskComplete.taskDB.endDate,
        endTime = this.taskComplete.taskDB.endTime,
        daysOfWeek = this.taskComplete.taskDB.daysOfWeek ?: emptyList(),
        reminderId = this.taskComplete.taskDB.reminderId,
        reminder = this.taskComplete.taskDB.reminder?.toReminder(),
        location = this.locationDB?.toLocation(),
        hasDone = this.taskComplete.hasDone,
        doneDates = this.taskComplete.doneDates,
        hasDeleted = this.taskComplete.hasDeleted,
        deletedDates = this.taskComplete.deletedDates
    )

fun Achievement.toAchievementDB(): AchievementDB =
    AchievementDB(
        achievementId = this.id,
        name = this.name,
        description = this.description,
        achievementGroupId = this.group?.id,
        endDate = this.endDate,
        doneDate = this.doneDate
    )

fun AchievementRelations.toAchievement(): Achievement =
    Achievement(
        id = this.achievementDB.achievementId,
        name = this.achievementDB.name,
        description = this.achievementDB.description,
        group = this.groupDB?.toGroup(),
        measurementType = when {
            !this.checkList.isNullOrEmpty() -> {
                MeasurementType.TaskDone(checkList = this.checkList.map { it.toCheckListItem() })
            }

            !this.percentageProgress.isNullOrEmpty() -> {
                MeasurementType.Percentage(percentageProgress = this.percentageProgress.map { it.toPercentageProgress() })
            }

            else -> null
        },
        endDate = this.achievementDB.endDate,
        doneDate = this.achievementDB.doneDate
    )

fun PercentageProgressDB.toPercentageProgress(): MeasurementType.Percentage.PercentageProgress =
    MeasurementType.Percentage.PercentageProgress(
        progress = this.progress,
        description = this.description,
        date = this.date,
        time = this.time
    )

fun MeasurementType.Percentage.PercentageProgress.toPercentageProgressDB(achievementId: Long): PercentageProgressDB =
    PercentageProgressDB(
        id = this.id,
        achievementPercentageProgressId = achievementId,
        progress = this.progress,
        description = this.description,
        date = this.date,
        time = this.time
    )
